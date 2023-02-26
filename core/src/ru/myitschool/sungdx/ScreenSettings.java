package ru.myitschool.sungdx;

import static ru.myitschool.sungdx.MainGame.SCR_HEIGHT;
import static ru.myitschool.sungdx.MainGame.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class ScreenSettings implements Screen {
    MainGame c;

    Texture imgBG;

    TextButton btnSound, btnMusic, btnLanguage, btnExit;

    public ScreenSettings(MainGame context){
        c = context;
        // создание изображений
        imgBG = new Texture("settingsBG.jpg");
        btnSound = new TextButton(c.fontLarge, "ЗВУК ВКЛ", 600, 650);
        btnMusic = new TextButton(c.fontLarge, "МУЗЫКА", 600, 550);
        btnLanguage = new TextButton(c.fontLarge, "ЧТО-ТО", 600, 450);
        btnExit = new TextButton(c.fontLarge, "ВЫХОД", 600, 350);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // обработка касаний экрана
        if(Gdx.input.justTouched()) {
            c.touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            c.camera.unproject(c.touch);
            if (btnSound.hit(c.touch.x, c.touch.y)) {
                c.screenGame.soundOn = !c.screenGame.soundOn;
                if(c.screenGame.soundOn)
                    btnSound.text = "ЗВУК ВКЛ.";
                else
                    btnSound.text="ЗВУК ВЫКЛ";
            }
            if(btnExit.hit(c.touch.x, c.touch.y)) {
                c.setScreen(c.screenIntro);
            }
        }
        // отрисовка всей графики
        c.camera.update();
        c.batch.setProjectionMatrix(c.camera.combined);
        c.batch.begin();
        c.batch.draw(imgBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        btnSound.font.draw(c.batch, btnSound.text, btnSound.x, btnSound.y);
        btnMusic.font.draw(c.batch, btnMusic.text, btnMusic.x, btnMusic.y);
        btnLanguage.font.draw(c.batch, btnLanguage.text, btnLanguage.x, btnLanguage.y);
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
