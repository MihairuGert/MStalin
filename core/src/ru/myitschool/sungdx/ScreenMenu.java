package ru.myitschool.sungdx;

import static ru.myitschool.sungdx.MainGame.SCR_HEIGHT;
import static ru.myitschool.sungdx.MainGame.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;

public class ScreenMenu implements Screen {
    MainGame c;

    Texture imgBG;

    TextButton btnPlay, btnSettings, btnAbout, btnExit;

    Music music;

    public ScreenMenu(MainGame context){
        c = context;
        // создание изображений
        imgBG = new Texture("menu_background.jpg");
        btnPlay = new TextButton(c.fontLarge, "РАССТРЕЛИВАТЬ", 650);
        btnSettings = new TextButton(c.fontLarge, "НАСТРОЙКИ", 550);
        btnAbout = new TextButton(c.fontLarge, "ОБ ИГРЕ", 450);
        btnExit = new TextButton(c.fontLarge, "ВЫХОД", 350);
        music = Gdx.audio.newMusic(Gdx.files.internal("menu.mp3"));
    }

    @Override
    public void show() {
        music.play();

    }

    @Override
    public void render(float delta) {
        // обработка касаний экрана
        if(Gdx.input.justTouched()) {
            c.touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            c.camera.unproject(c.touch);
            if(btnPlay.hit(c.touch.x, c.touch.y)) {
                music.pause();
                c.setScreen(c.screenGame);
            }
            if(btnSettings.hit(c.touch.x, c.touch.y)) {
                c.setScreen(c.screenSettings);
            }
            if(btnExit.hit(c.touch.x, c.touch.y)) {
                music.dispose();
                Gdx.app.exit();
            }
        }
        // отрисовка всей графики
        c.camera.update();
        c.batch.setProjectionMatrix(c.camera.combined);
        c.batch.begin();
        c.batch.draw(imgBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        btnPlay.font.draw(c.batch, btnPlay.text, btnPlay.x, btnPlay.y);
        btnSettings.font.draw(c.batch, btnSettings.text, btnSettings.x, btnSettings.y);
        btnAbout.font.draw(c.batch, btnAbout.text, btnAbout.x, btnAbout.y);
        btnExit.font.draw(c.batch, btnExit.text, btnExit.x, btnExit.y);
        c.batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
