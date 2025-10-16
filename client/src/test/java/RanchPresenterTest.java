import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import views.common.context.MenuContext;
import views.common.ui.Panel;
import views.ranch.RanchPresenter;
import views.ranch.RanchView;

import static org.mockito.Mockito.*;

class RanchPresenterTest {

    private RanchView view;
    private MenuContext menuContext;
    private RanchPresenter presenter;

    private Panel dummyPanel;

    @BeforeEach
    void setup() {
        view = mock(RanchView.class);
        menuContext = mock(MenuContext.class);
        presenter = new RanchPresenter(view, menuContext);
        dummyPanel = mock(Panel.class);
    }

    @Test
    void raceButton_showsRacePanel() {
        presenter.onRaceButtonClicked(dummyPanel);
        verify(view).hideAllPanels();
        verify(view).showPanel(dummyPanel);
        verifyNoMoreInteractions(view, menuContext);
    }

    @Test
    void shopButton_showsShopPanel() {
        presenter.onShopButtonClicked(dummyPanel);
        verify(view).hideAllPanels();
        verify(view).showPanel(dummyPanel);
        verifyNoMoreInteractions(view, menuContext);
    }

    @Test
    void singleplayer_setsModeAndShowsPanel() {
        presenter.onSingleplayerClicked();
        verify(menuContext).setRaceMode(MenuContext.RaceMode.SINGLEPLAYER);
        verify(view).hideAllPanels();
        verify(view).navigateToRace("");
        verifyNoMoreInteractions(view, menuContext);
    }

    @Test
    void multiplayer_setsModeAndShowsPanel() {
        presenter.onMultiplayerClicked(dummyPanel);
        verify(menuContext).setRaceMode(MenuContext.RaceMode.MULTIPLAYER);
        verify(view).hideAllPanels();
        verify(view).showPanel(dummyPanel);
        verifyNoMoreInteractions(view, menuContext);
    }
}
