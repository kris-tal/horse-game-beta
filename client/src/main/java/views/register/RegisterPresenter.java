package views.register;

import data.auth.AuthCode;
import services.auth.AuthService;
import services.managers.SessionManager;

public class RegisterPresenter {
    private final RegisterView view;
    private final AuthService authService;

    public RegisterPresenter(RegisterView view, AuthService authService) {
        this.view = view;
        this.authService = authService;
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
            SessionManager.getInstance().startSession(result.token);
            view.showRegisterSuccess();
            view.navigateToRanch();
        }
    }

    public void onLoginLabelClicked() {
        view.navigateToLogin();
    }
}
