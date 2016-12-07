package com.fogok.explt.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fogok.explt.objects.StoryNarrator;
import com.fogok.explt.objects.uiwidgets.basewidgets.TextBlock;
import com.fogok.explt.objects.uiwidgets.buttons.Button;
import com.fogok.explt.objects.uiwidgets.buttons.ButtonActions;
import com.fogok.explt.utils.GMUtils;
import com.fogok.explt.utils.Localization;
import com.fogok.explt.utils.Prefers;

/**
 * Created by FOGOK on 06.12.2016 1:46.
 * Если ты это читаешь, то знай, что этот код хуже
 * кожи разлагающегося бомжа лежащего на гнилой
 * лавочке возле остановки автобуса номер 985
 */
public class StartScreen {

    private boolean isStart = false, isStartedStart = false;
    private Sprite whiteScreen;
    private Button soundB, musicB;
    private TextBlock startGameText;

    public StartScreen(AtlasLoader atlasLoader) {

        final float size = (int)(Gdx.graphics.getPpcX() * 1.18f);
        final float otst = size * 0.2f;
        soundB = new Button(atlasLoader, ButtonActions.All.SOUND, otst, otst, size, size);
        musicB = new Button(atlasLoader, ButtonActions.All.MUSIC, Gdx.graphics.getWidth() - otst - size, otst, size, size);

        startGameText = new TextBlock(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, Localization.getText(48));
        startGameText.setPositionToCenter();

        createWhiteScreen();
        SoundCore.playMusic(SoundCore.Musics.MainMenu);
    }

    private boolean isTouched = false;
    private void calcM(){
        if (Gdx.input.isTouched()){ //если на экран нажимает палец
            isTouched = startGameText.getBounds().contains(
                    Gdx.input.getX(),
                    (Gdx.graphics.getHeight() - Gdx.input.getY())
            );  ///определяем,  касается ли палец кнопки или нет

        }else{              //при отпускании кнопки
            if (isTouched && !isStartedStart)      ///если при отпускании кнопки палец находился на кнопке, то выполняем действие
                activateGame();

            isTouched = false;  //делаем так, чтобы действие не выполнилось ещё раз
        }
    }

    private void activateGame(){
        isStartedStart = true;
        SoundCore.playSound(SoundCore.Sounds.Notify);
        playSoundRestart = false;
        if (Prefers.getInt(Prefers.KeyStateStory) == 0)
            StoryNarrator.setStory(1);
        else{
            itersWhiteScreen2 = 1f;
            StoryNarrator.setCurrentText(22);
            StoryNarrator.setIsShow(false);
            isStart = true;

        }
    }


    public void draw(SpriteBatch batch){
        if (itersWhiteScreen2 != 1f){
            drawWhiteScreen(batch);
            soundB.draw(batch);
            musicB.draw(batch);
            startGameText.draw(batch);
            if (!isStartedStart)
                calcM();
        }
        if (StoryNarrator.getCurrentStory() < 9)
            drawWhiteScreen2(batch);
        else
            drawWhiteScreen(batch);


    }

    private float itersWhiteScreen2 = 0f;
    private boolean playSoundRestart;
    private void drawWhiteScreen2(SpriteBatch batch){
        float alpha = GMUtils.normalizeOneZero(itersWhiteScreen2);
        whiteScreen.setAlpha(alpha);
        whiteScreen.draw(batch);

        if (isStartedStart)
            itersWhiteScreen2 += Gdx.graphics.getDeltaTime();



        if (itersWhiteScreen2 > 1f){
            itersWhiteScreen2 = 1f;
            SoundCore.stopMusic(SoundCore.Musics.MainMenu);
            SoundCore.setVolume(1f);
            SoundCore.playMusic(SoundCore.Musics.Game);
            isStart = true;
        }else{
//            if (!playSoundRestart && isStartedStart){
//                SoundCore.setVolume(1f);
//                SoundCore.playSound(SoundCore.Sounds.Restart);
//                playSoundRestart = true;
//            }
            SoundCore.setVolume(1f - itersWhiteScreen2);

        }
    }

    private float itersWhiteScreen = 1f;
    private void drawWhiteScreen(SpriteBatch batch){
        float alpha = GMUtils.normalizeOneZero(itersWhiteScreen);
        whiteScreen.setAlpha(alpha);
        whiteScreen.draw(batch);

        if (itersWhiteScreen2 == 1f)
            itersWhiteScreen -= Gdx.graphics.getDeltaTime();



        if (itersWhiteScreen < 0f){
            itersWhiteScreen = 0f;
            if (!SoundCore.getGame().isPlaying()){
                SoundCore.stopMusic(SoundCore.Musics.MainMenu);
                SoundCore.setVolume(1f);
                SoundCore.playMusic(SoundCore.Musics.Game);
                SoundCore.playSound(SoundCore.Sounds.Restart);
            }
        }else{
            if (!SoundCore.getGame().isPlaying()){
                SoundCore.setVolume(1f - itersWhiteScreen2);
            }
            if (!playSoundRestart && StoryNarrator.getCurrentStory() > 8){
                SoundCore.setVolume(1f);
                SoundCore.playSound(SoundCore.Sounds.Restart);
                playSoundRestart = true;
            }
        }
    }

    private void createWhiteScreen(){
        Pixmap pixmap = new Pixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fillRectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        whiteScreen = new Sprite(new Texture(pixmap));
        pixmap.dispose();
    }

    public boolean getStart(){
        return isStart;
    }

    public boolean isEndedH(){
        return itersWhiteScreen == 0f;
    }

    public void dispose() {
        whiteScreen.getTexture().dispose();
    }
}
