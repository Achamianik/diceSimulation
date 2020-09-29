package com.onwelo.dice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onwelo.dice.api.RollBasicStatistic;
import com.onwelo.dice.api.RollsDistributionStatistic;
import com.onwelo.dice.api.RollsStatistic;
import com.onwelo.dice.domain.DiceGroup;
import com.onwelo.dice.domain.GroupSizeKey;
import com.onwelo.dice.domain.RolesSession;
import com.onwelo.dice.statistic.RollStatisticFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ContextConfiguration(classes = {DiceStatisticController.class, DiceValidationAdviceHandler.class})
@WebMvcTest
class DiceStatisticControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RollStatisticFacade rollStatisticFacade;

    @Autowired
    private ObjectMapper objectMapper;

    @Captor
    private ArgumentCaptor<Integer> intCaptor;

    @Test
    public void get_statistic_ok() throws Exception {
        LinkedHashMap<GroupSizeKey, RollBasicStatistic> roles = new LinkedHashMap<>();
        roles.put(new GroupSizeKey(3, 6), new RollBasicStatistic(5, 20));
        roles.put(new GroupSizeKey(1, 6), new RollBasicStatistic(1, 50));
        roles.put(new GroupSizeKey(2, 4), new RollBasicStatistic(2, 30));

        when(rollStatisticFacade.getStatistic())
                .thenReturn(new RollsStatistic(roles));

        mockMvc.perform(get("/dice/statistic/get"))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @CsvSource({
            "1, 1, rollDistribution.diceSize: must be greater than or equal to 4",
            "0, 6, rollDistribution.diceNumber: must be greater than or equal to 1"
    })
    public void get_rollDistribution_validation(int diceNumber, int diceSide, String validationMessage) throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/dice/statistic/rollDistribution/{diceNumber}/{diceSize}", diceNumber, diceSide))
                .andExpect(status().isInternalServerError())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        DiceValidationAdviceHandler.ErrorResponse response = objectMapper.readValue(contentAsString, DiceValidationAdviceHandler.ErrorResponse.class);
        assertThat(response.getMessage()).isEqualTo(validationMessage);
        assertThat(response.getLocalDateTime()).isNotNull();
        assertThat(response.getStatus()).isEqualTo("Validation Error");
    }

    @ParameterizedTest
    @CsvSource({
            "3, 6",
            "1, 4"
    })
    public void get_rollDistribution_ok(int diceNumber, int diceSize) throws Exception {

        when(rollStatisticFacade.getRollDistribution(eq(3), eq(6)))
                .thenReturn(new RollsDistributionStatistic(Map.of(3, new BigDecimal("1.66"))));

        when(rollStatisticFacade.getRollDistribution(eq(1), eq(4)))
                .thenReturn(new RollsDistributionStatistic(new RolesSession(new DiceGroup(diceSize, diceNumber))));

        mockMvc.perform(get("/dice/statistic/rollDistribution/{diceNumber}/{diceSize}", diceNumber, diceSize))
                .andExpect(status().isOk());

        verify(rollStatisticFacade, times(1)).getRollDistribution(intCaptor.capture(), intCaptor.capture());

        assertThat(intCaptor.getAllValues())
                .containsExactly(diceNumber, diceSize);
    }
}