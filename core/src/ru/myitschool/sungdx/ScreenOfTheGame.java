package ru.myitschool.sungdx;

import static ru.myitschool.sungdx.MainGame.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;

public class ScreenOfTheGame implements Screen {
    MainGame c;
    InputKeyboard inputKeyboard;

    Texture[] imgMosq = new Texture[11];
    Texture imgBG;
    Sound[] sndMosq = new Sound[3];


    EnemyOfTheNation[] mosq = new EnemyOfTheNation[5];
    Player[] players = new Player[6];
    Player player;
    int frags;
    long timeStart, timeCurrent;
    public static final int PLAY_GAME = 0, ENTER_NAME = 1, SHOW_TABLE = 2;
    int condition = PLAY_GAME;
    TextButton btnExit;
    boolean soundOn = true;

    public ScreenOfTheGame(MainGame context){
        c = context;
        inputKeyboard = new InputKeyboard(SCR_WIDTH, SCR_HEIGHT, 10);

        // создание изображений
        imgBG = new Texture("background.jpg");
        for (int i = 0; i < imgMosq.length; i++) {
            imgMosq[i] = new Texture("mosq"+i+".png");
        }

        // создание звуков
        for (int i = 0; i < sndMosq.length; i++) {
            sndMosq[i] = Gdx.audio.newSound(Gdx.files.internal("mosq"+i+".mp3"));
        }



        // создание игроков для таблицы рекордов
        for (int i = 0; i < players.length; i++) {
            players[i] = new Player("Никто", 0);
        }
        player = new Player("Gamer", 0);

        btnExit = new TextButton(c.font, "Exit", 200);
    }

    @Override
    public void show() {
        gameStart();
        Gdx.input.setCatchKey(Input.Keys.BACK,true);
    }

    @Override
    public void render(float delta) {
    // обработка касаний экрана
        if(Gdx.input.justTouched()){
            c.touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            c.camera.unproject(c.touch);
            if(condition == SHOW_TABLE){
                if(btnExit.hit(c.touch.x, c.touch.y)) c.setScreen(c.screenIntro);
                else gameStart();
            }
            if(condition == PLAY_GAME){
                for (int i = mosq.length - 1; i >= 0; i--) {
                    if (mosq[i].isAlive && mosq[i].hit(c.touch.x, c.touch.y)) {
                        frags++;
                        if(soundOn)
                            sndMosq[0].play();
                        if (frags == mosq.length)
                            gameOver();
                        break;
                    }
                }
            }
            if(condition == ENTER_NAME){
                if(inputKeyboard.endOfEdit(c.touch.x, c.touch.y)){
                    player.name = inputKeyboard.getText();
                    players[players.length-1].time = player.time;
                    players[players.length-1].name = player.name;
                    sortPlayers();
                    saveTableOfRecords();
                    condition = SHOW_TABLE;
                }
            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.BACK)){
            c.setScreen(c.screenIntro);
        }

        // события игры
        for (int i = 0; i < mosq.length; i++) {
            mosq[i].move();
        }
        if(condition == PLAY_GAME) {
            timeCurrent = TimeUtils.millis() - timeStart;
        }

        // отрисовка всей графики
        c.camera.update();
        c.batch.setProjectionMatrix(c.camera.combined);
        c.batch.begin();
        c.batch.draw(imgBG, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        for (int i = 0; i < mosq.length; i++) {
            c.batch.draw(imgMosq[mosq[i].faza], mosq[i].getX(), mosq[i].getY(), mosq[i].width, mosq[i].height, 0, 0, 500, 500, mosq[i].isFlip(), false);
        }
        c.font.draw(c.batch, "KILLS: "+frags, 10, SCR_HEIGHT-10);
        c.font.draw(c.batch, timeToString(timeCurrent), SCR_WIDTH-200, SCR_HEIGHT-10);
        if(condition == ENTER_NAME) inputKeyboard.draw(c.batch);
        if(condition == SHOW_TABLE) {
            c.font.draw(c.batch, tableOfRecordsToString(), 0, SCR_HEIGHT / 4f * 3, SCR_WIDTH, Align.center, true);
            btnExit.font.draw(c.batch, btnExit.text, btnExit.x, btnExit.y);
        }
        c.batch.end();
    }

    String timeToString(long time){
        return time/1000/60/60 + ":" + time/1000/60%60/10 + time/1000/60%60%10 + ":" + time/1000%60/10 + time/1000%60%10;
    }

    void gameOver(){
        condition = ENTER_NAME;
        player.time = timeCurrent;
    }

    void gameStart(){
        condition = PLAY_GAME;
        frags = 0;
        timeStart = TimeUtils.millis();
        // создание комаров
        for (int i = 0; i < mosq.length; i++) {
            mosq[i] = new EnemyOfTheNation();
        }
        loadTableOfRecords();
    }

    void sortPlayers(){
        for (int i = 0; i < players.length; i++) if(players[i].time == 0) players[i].time = Long.MAX_VALUE;

        for (int j = 0; j < players.length; j++) {
            for (int i = 0; i < players.length-1; i++) {
                if(players[i].time>players[i+1].time){
                    Player c = players[i];
                    players[i] = players[i+1];
                    players[i+1] = c;
                }
            }
        }
        for (int i = 0; i < players.length; i++) if(players[i].time == Long.MAX_VALUE) players[i].time = 0;
    }

    String tableOfRecordsToString(){
        String s = "";
        for (int i = 0; i < players.length-1; i++) {
            s += players[i].name+points(players[i].name, 13)+timeToString(players[i].time)+"\n";
        }
        return s;
    }

    void saveTableOfRecords(){
        try {
            Preferences pref = Gdx.app.getPreferences("TableOfRecords");
            for (int i = 0; i < players.length; i++) {
                pref.putString("name"+i, players[i].name);
                pref.putLong("time"+i, players[i].time);
            }
            pref.flush();
        } catch (Exception e){
        }
    }

    void loadTableOfRecords(){
        try {
            Preferences pref = Gdx.app.getPreferences("TableOfRecords");
            for (int i = 0; i < players.length; i++) {
                if(pref.contains("name"+i))	players[i].name = pref.getString("name"+i, "null");
                if(pref.contains("time"+i))	players[i].time = pref.getLong("time"+i, 0);
            }
        } catch (Exception e){
        }
    }

    String points(String name, int length){
        int n = length-name.length();
        String s = "";
        for (int i = 0; i < n; i++) s += ".";
        return s;
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
        Gdx.input.setCatchKey(Input.Keys.BACK,false);
    }

    @Override
    public void dispose() {

    }
}
