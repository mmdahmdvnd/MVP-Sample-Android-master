package com.mmd.mvpexample;

import static org.mockito.Mockito.*;
import android.view.View;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
//@Config(sdk = {28}) // می‌توانید نسخه SDK دلخواه را انتخاب کنید
@Config(manifest=Config.NONE)
public class LoginPresenterTest {

    @Mock
    private ILoginResult loginResult;

    private LoginPresenter presenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        presenter = new LoginPresenter(loginResult);
    }

    @Test
    public void testDoLogin_successfulLogin() {
        presenter.doLogin("validUser", "validPassword");
        verify(loginResult, times(1)).onSetProgressBarVisibility(View.VISIBLE);
        verify(loginResult, after(2000).times(1)).onLoginResult(true, 0);
    }

    @Test
    public void testDoLogin_failedLogin() {
        presenter.doLogin("", "validPassword");
        verify(loginResult, times(1)).onSetProgressBarVisibility(View.VISIBLE);
        verify(loginResult, after(2000).times(1)).onLoginResult(false, -1);
    }

    @Test
    public void testSetProgressBarVisibility() {
        presenter.setProgressBarVisiblity(View.VISIBLE);
        verify(loginResult, times(1)).onSetProgressBarVisibility(View.VISIBLE);
    }
}
