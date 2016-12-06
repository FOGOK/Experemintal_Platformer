package com.fogok.explt.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fogok.explt.objects.uiwidgets.basewidgets.TextBlock;
import com.fogok.explt.utils.Localization;

/**
 * Created by FOGOK on 05.12.2016 1:00.
 * Если ты это читаешь, то знай, что этот код хуже
 * кожи разлагающегося бомжа лежащего на гнилой
 * лавочке возле остановки автобуса номер 985
 */
public class StoryNarrator {

    private static int startText, endText, currentText, lastCurrentText;
    private static float showTime, showMax;
    private static boolean isTimedTicked;

    private static boolean isShow;

    private static TextBlock upGameText;


    public static void init(int currentStory){

        upGameText = new TextBlock(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() * 0.95f, Localization.getText(0));
        setStory(currentStory);
        upGameText.setCustomCff(0.23f);
        upGameText.setPositionToCenter();
        lastCurrentText = -1;
        isShow = true;
    }

    public static void setStory(int k){
        isShow = true;
        switch (k){
            case 0:
                startText = endText = 0;
                break;
            case 1:
                startText = 1;
                endText = 21;
                break;
            case 2:
                startText = 22;
                endText = 26;
                break;
            case 3:
                startText = endText = 27;
                break;
            case 4:
                startText = 28;
                endText = 31;
                break;
            case 5:
                startText = 32;
                endText = 35;
                break;
            case 6:
                startText = endText = 36;
                break;
            case 7:
                startText = endText = 37;
                break;
            case 8:
                startText = 38;
                endText = 39;
                break;
            case 9:
                startText = 40;
                endText = 46;
                break;
        }
        currentText = startText;
        isTimedTicked = false;
        showTime = 0f;
    }

    public static void drawAndNarrate(SpriteBatch batch){
        if (isShow){
            calcTime();
            upGameText.draw(batch);
        }
    }

    private static void calcTime(){
        if (currentText != 0){
            if (!isTimedTicked){
                isTimedTicked = true;
                showMax = Localization.getText(currentText).length() * 0.08f;
                showMax = Math.max(showMax, 2f);
            }
            if (showTime < showMax) {
                showTime += Gdx.graphics.getDeltaTime();
            }else{
                if (currentText < endText)
                    currentText++;
                else
                    isShow = false;


                showTime = 0f;
                isTimedTicked = false;
            }


        }
        if (lastCurrentText != currentText){
            upGameText.setText(Localization.getText(currentText));
            upGameText.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() * 0.95f);

            if (currentText < 9)
                upGameText.setBlackColor(true);
            else
                upGameText.setBlackColor(false);

            upGameText.setPositionToCenter();
        }
        lastCurrentText = currentText;
    }

    public static int getCurrentStory(){
        return currentText;
    }

}
