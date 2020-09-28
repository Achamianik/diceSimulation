package com.onwelo.dice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onwelo.dice.dice.DiceRollerFacade;
import com.onwelo.dice.domain.DiceGroup;
import com.onwelo.dice.domain.RolesSession;
import com.onwelo.dice.domain.RollResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ContextConfiguration(classes = {DiceController.class, DiceValidationAdviceHandler.class})
@WebMvcTest
class DiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DiceRollerFacade diceRollerFacade;

    @ParameterizedTest
    @CsvSource({
            "1, 1, 1, rollDices.side: must be greater than or equal to 4",
            "4, 0, 5, rollDices.diceNumber: must be greater than or equal to 1",
            "4, 1, 0, rollDices.rolls: must be greater than or equal to 1",
    })
    public void exceptionTest(int side, int dice, int rollNumber, String errorMessage) throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/dice/roll/{side}/{diceNumber}/{rolls}", side, dice, rollNumber))
                .andExpect(status().isInternalServerError())
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        DiceValidationAdviceHandler.ErrorResponse response = objectMapper.readValue(contentAsString, DiceValidationAdviceHandler.ErrorResponse.class);
        assertThat(response.getMessage()).isEqualTo(errorMessage);
        assertThat(response.getLocalDateTime()).isNotNull();
        assertThat(response.getStatus()).isEqualTo("Validation Error");
    }

    @Test
    public void response_ok() throws Exception {

        DiceGroup diceGroup = new DiceGroup(6, 1);
        RolesSession role = new RolesSession(diceGroup);
        role.increment(new RollResult(2));
        role.increment(new RollResult(1));
        role.increment(new RollResult(6));
        role.increment(new RollResult(5));
        role.increment(new RollResult(5));
        when(diceRollerFacade.createDiceGroup(eq(6), eq(1)))
                .thenReturn(diceGroup);
        when(diceRollerFacade.roleDices(diceGroup, 2))
                .thenReturn(role);

        MvcResult mvcResult = mockMvc.perform(get("/dice/roll/{side}/{diceNumber}/{rolls}", 6, 1, 2))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString())
                .isEqualTo("{\"result\":{\"1\":1,\"2\":1,\"3\":0,\"4\":0,\"5\":2,\"6\":1}}");


    }

}