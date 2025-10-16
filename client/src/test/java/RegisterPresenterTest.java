

import data.auth.AuthCode;
import data.auth.AuthResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.auth.AuthService;
import services.managers.SessionManagerPort;
import views.register.RegisterPresenter;
import views.register.RegisterView;

import static org.mockito.Mockito.*;

class RegisterPresenterTest {

    private RegisterView view;
    private AuthService authService;
    private SessionManagerPort sessionManager;
    private RegisterPresenter presenter;

    @BeforeEach
    void setup() {
        view = mock(RegisterView.class);
        authService = mock(AuthService.class);
        sessionManager = mock(SessionManagerPort.class);
        presenter = new RegisterPresenter(view, authService, sessionManager);
    }

    @Test
    void registerFailure_passwordsMismatch() {
        presenter.onRegisterClicked("username", "password", "kefir");

        verify(view).showRegisterFailure();
        verifyNoMoreInteractions(view);
        verifyNoInteractions(sessionManager);
    }

    @Test
    void registerFailure_usernameTaken() {
        var result = new AuthResult(AuthCode.USERNAME_TAKEN, null);
        when(authService.register("username", "password")).thenReturn(result);

        presenter.onRegisterClicked("username", "password", "password");

        verify(view).showRegisterFailure();
        verify(view, never()).showRegisterSuccess();
        verify(view, never()).navigateToRanch();
        verifyNoInteractions(sessionManager);
    }

    @Test
    void registerFailure_serverError() {
        var result = new AuthResult(AuthCode.SERVER_ERROR, null);
        when(authService.register("username", "password")).thenReturn(result);

        presenter.onRegisterClicked("username", "password", "password");

        verify(view).showRegisterFailure();
        verify(view, never()).showRegisterSuccess();
        verify(view, never()).navigateToRanch();
        verifyNoInteractions(sessionManager);
    }

    @Test
    void registerFailure_timeout() {
        var result = new AuthResult(AuthCode.TIMEOUT, null);
        when(authService.register("username", "password")).thenReturn(result);

        presenter.onRegisterClicked("username", "password", "password");

        verify(view).showRegisterFailure();
        verify(view, never()).showRegisterSuccess();
        verify(view, never()).navigateToRanch();
        verifyNoInteractions(sessionManager);
    }

    @Test
    void registerFailure_interrupted() {
        var result = new AuthResult(AuthCode.INTERRUPTED, null);
        when(authService.register("username", "password")).thenReturn(result);

        presenter.onRegisterClicked("username", "password", "password");

        verify(view).showRegisterFailure();
        verify(view, never()).showRegisterSuccess();
        verify(view, never()).navigateToRanch();
        verifyNoInteractions(sessionManager);
    }

    @Test
    void registerFailure_invalidInput() {
        var result = new AuthResult(AuthCode.INVALID_INPUT, null);
        when(authService.register("username", "password")).thenReturn(result);

        presenter.onRegisterClicked("username", "password", "password");

        verify(view).showRegisterFailure();
        verify(view, never()).showRegisterSuccess();
        verify(view, never()).navigateToRanch();
        verifyNoInteractions(sessionManager);
    }

    @Test
    void registerSuccess_navigateToRanch() {
        var result = new AuthResult(AuthCode.SUCCESS, "reg-token");
        when(authService.register("username", "password")).thenReturn(result);

        presenter.onRegisterClicked("username", "password", "password");

        verify(sessionManager).startSession("reg-token");
        verify(view).showRegisterSuccess();
        verify(view).navigateToRanch();
        verify(view, never()).showRegisterFailure();
    }

    @Test
    void loginLabelClicked_navigatesToLogin() {
        presenter.onLoginLabelClicked();
        verify(view).navigateToLogin();
    }
}
