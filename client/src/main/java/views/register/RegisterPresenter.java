package views.register;

import data.auth.AuthCode;
import services.auth.AuthService;
import services.managers.SessionManagerPort;

public class RegisterPresenter {
    private final RegisterView view;
    private final AuthService authService;
    private final SessionManagerPort sessionManager;

    public RegisterPresenter(RegisterView view, AuthService authService, SessionManagerPort sessionManager) {
        this.view = view;
        this.authService = authService;
        this.sessionManager = sessionManager;
    }

    public void onRegisterClicked(String username, String password, String passwordRepeated) {
        if (!password.equals(passwordRepeated)) {
            view.showRegisterFailure();
            return;
        }
        var result = authService.register(username, password);
        if (result.returnedCode != AuthCode.SUCCESS) {
            view.showRegisterFailure();
        } else {
            sessionManager.startSession(result.token);
            view.showRegisterSuccess();
            view.navigateToRanch();
        }
    }

    public void onLoginLabelClicked() {
        view.navigateToLogin();
    }
}
