package views.login;

public interface LoginView {
    void showLoginSuccess();
    void showLoginFailure();
    void navigateToRegister();
    void navigateToRanch();
    void exitGame();
}
