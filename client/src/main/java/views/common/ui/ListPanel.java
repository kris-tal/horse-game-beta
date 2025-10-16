package views.common.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import java.util.List;

public abstract class ListPanel<T> extends Panel {

    protected ScrollPane scrollPane;
    protected Table listTable;
    protected T selectedItem;
    protected Table lastSelectedRow = null;

    public ListPanel(float width, float height) {
        super(width, height);
        super.build();
    }

    @Override
    protected void addContent(float width, float height) {
        listTable = new Table();
        listTable.top();

        scrollPane = new ScrollPane(listTable);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);

        content.row();
        content.add(scrollPane).width(width * 0.8f).height(height * 0.5f).center().row();
    }

    protected abstract void populateRow(Table row, T item);

    protected void onEscapePressed() {
    }

    protected void onItemSelected(T item, Table row) {
        if (lastSelectedRow != null)
            lastSelectedRow.setColor(Color.WHITE);

        row.setColor(Color.LIGHT_GRAY);
        lastSelectedRow = row;
        selectedItem = item;
    }

    protected void populateList(List<T> items) {
        listTable.clear();
        selectedItem = null;
        lastSelectedRow = null;

        Drawable alternate = UIFactory.createDefaultBackgroundDrawable();
        Drawable defaultBg = UIFactory.createDefaultBackgroundDrawable();

        for (int i = 0; i < items.size(); i++) {
            T item = items.get(i);
            Table row = new Table();

            if (i % 2 == 0) {
                row.setBackground(defaultBg);
            } else {
                row.setBackground(alternate);
            }

            populateRow(row, item);

            row.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    onItemSelected(item, row);
                }
            });
            row.addListener(new InputListener() {
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand);
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
                }
            });

            listTable.add(row).expandX().fillX().pad(5).center().row();

            if (i == 0) {
                onItemSelected(item, row);
            }
        }
    }

    public ScrollPane getScrollPane() {
        return scrollPane;
    }
}
