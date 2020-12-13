package pl.jmiernowski;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.web.servlet.ModelAndView;

@SpringBootTest
class GlobalErrorHandlerTest {

    @Autowired
    GlobalErrorHandler globalErrorHandler;

    IncorrectResultSizeDataAccessException incorrectResultSizeDataAccessException = new IncorrectResultSizeDataAccessException(1);
    RuntimeException runtimeException = new RuntimeException();
    @Test
    void shouldHandleAnyIncorrectResultSizeDataAccessExceptionException(){
    //given
    //when
        ModelAndView mav = globalErrorHandler.handleAnyIncorrectResultSizeDataAccessExceptionException(incorrectResultSizeDataAccessException);
        //then
        ModelAndViewAssert.assertViewName(mav, "contactWithAdmin.html");
    }

    @Test
    void shouldHandleAnyRuntimeException(){
        //given
        //when
        ModelAndView mav = globalErrorHandler.handleAnyRuntimeException(runtimeException);
        //then
        ModelAndViewAssert.assertViewName(mav, "contactWithAdmin.html");
    }

}
