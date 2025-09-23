import data.auth.AuthCode;
import data.auth.AuthResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.auth.AuthService;
import views.login.LoginPresenter;
import views.login.LoginView;

import static org.mockito.Mockito.*;

class LoginPresenterTest {

    private LoginView view;
    private AuthService authService;
    private LoginPresenter presenter;

    @BeforeEach
    void setup() {
        view = mock(LoginView.class);
        authService = mock(AuthService.class);
        presenter = new LoginPresenter(view, authService);
    }

    @Test
    void loginSuccess_navigateToRanch() {
        var result = new AuthResult(AuthCode.SUCCESS, null);
        when(authService.login("username", "password")).thenReturn(result);

        presenter.onLoginClicked("username", "password");

        verify(view).showLoginSuccess();
        verify(view).navigateToRanch();
        verify(view, never()).showLoginFailure();
    }

    @Test
    void loginFailure_usernameNotFound() {
        var result = new AuthResult(AuthCode.USER_NOT_FOUND, null);
        when(authService.login("username", "password")).thenReturn(result);

        presenter.onLoginClicked("username", "password");

        verify(view).showLoginFailure();
        verify(view, never()).showLoginSuccess();
        verify(view, never()).navigateToRanch();
    }

    @Test
    void loginFailure_wrongPassword() {
        var result = new AuthResult(AuthCode.WRONG_PASSWORD, null);
        when(authService.login("username", "password")).thenReturn(result);

        presenter.onLoginClicked("username", "password");

        verify(view).showLoginFailure();
        verify(view, never()).showLoginSuccess();
        verify(view, never()).navigateToRanch();
    }

    @Test
    void loginFailure_serverError() {
        var result = new AuthResult(AuthCode.SERVER_ERROR, null);
        when(authService.login("username", "password")).thenReturn(result);

        presenter.onLoginClicked("username", "password");

        verify(view).showLoginFailure();
        verify(view, never()).showLoginSuccess();
        verify(view, never()).navigateToRanch();
    }

    @Test
    void loginFailure_timeout() {
        var result = new AuthResult(AuthCode.TIMEOUT, null);
        when(authService.login("username", "password")).thenReturn(result);

        presenter.onLoginClicked("username", "password");

        verify(view).showLoginFailure();
        verify(view, never()).showLoginSuccess();
        verify(view, never()).navigateToRanch();
    }

    @Test
    void loginFailure_interrupted() {
        var result = new AuthResult(AuthCode.INTERRUPTED, null);
        when(authService.login("username", "password")).thenReturn(result);

        presenter.onLoginClicked("username", "password");

        verify(view).showLoginFailure();
        verify(view, never()).showLoginSuccess();
        verify(view, never()).navigateToRanch();
    }

    @Test
    void loginFailure_invalidInput() {
        var result = new AuthResult(AuthCode.INVALID_INPUT, null);
        when(authService.login("username", "password")).thenReturn(result);

        presenter.onLoginClicked("username", "password");

        verify(view).showLoginFailure();
        verify(view, never()).showLoginSuccess();
        verify(view, never()).navigateToRanch();
    }

    @Test
    void registerClicked_navigatesToRegister() {
        presenter.onRegisterClicked();
        verify(view).navigateToRegister();
    }

    @Test
    void exitPressed_exitsGame() {
        presenter.onExitPressed();
        verify(view).exitGame();
    }
}
