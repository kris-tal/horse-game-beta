package views.login;

import data.auth.AuthCode;
import services.auth.AuthService;
import services.managers.SessionManagerPort;

public class LoginPresenter {
    private final LoginView view;
    private final AuthService authService;
    private final SessionManagerPort sessionManager;

    public LoginPresenter(LoginView view, AuthService authService, SessionManagerPort sessionManager) {
        this.view = view;
        this.authService = authService;
        this.sessionManager = sessionManager;
    }

    public void onLoginClicked(String username, String password) {
        var result = authService.login(username, password);
        if (result.returnedCode == AuthCode.SUCCESS) {
            sessionManager.startSession(result.token);
            view.showLoginSuccess();
            view.navigateToRanch();
        } else {
            view.showLoginFailure();
        }
    }

    public void onRegisterClicked() {
        view.navigateToRegister();
    }

    public void onExitPressed() {
        view.exitGame();
    }
}
