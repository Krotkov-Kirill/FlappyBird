package ru.samsung.gamestudio.characters;

import static ru.samsung.gamestudio.MyGdxGame.SCR_HEIGHT;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Bird {

    int x, y;
    int width, height;

    Sound jumpSound = Gdx.audio.newSound(Gdx.files.internal("flap.wav"));
    public Sound scoreSound = Gdx.audio.newSound(Gdx.files.internal("point.wav"));


    private float y0;               // начальная высота
    private float v0 = 450f;        // начальная скорость прыжка
    private float gravity = 1000f;   // ускорение (аркадное)
    private float timeSinceJump = 0f;

    int frameCounter;
    Texture[] framesArray;

    public Bird(int x, int y, int speed, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        frameCounter = 0;

        framesArray = new Texture[]{
                new Texture("birdTiles/bird0.png"),
                new Texture("birdTiles/bird1.png"),
                new Texture("birdTiles/bird2.png"),
                new Texture("birdTiles/bird1.png"),
        };

        y0 = y;
    }

    public void setY(int y) {
        this.y = y;
        y0 = y;
    }

    public void onClick() {
        y0 = y;                 // текущая высота — старт прыжка
        v0 = 600f;              // скорость прыжка
        timeSinceJump = 0f;     // сброс таймера
        jumpSound.play();       // звук
    }


    public void fly(float delta) {
        timeSinceJump += delta;

        // Квадратичная формула движения: y = y0 + v0 * t - 0.5 * g * t^2
        float newY = y0 + v0 * timeSinceJump - 0.5f * gravity * timeSinceJump * timeSinceJump;
        y = (int) newY;

        // Защита от падения ниже экрана
        if (y < 0) {
            y = 0;
            timeSinceJump = 0;
            y0 = 0;
        }

        if (y + height > SCR_HEIGHT) {
            y = SCR_HEIGHT - height;   // ставим ровно под потолок
            v0 = -Math.abs(v0) * 0.7f; // отражаем скорость вверх, но уменьшаем (0.7 = коэффициент упругости)
            timeSinceJump = 0f;        // сбрасываем таймер, чтобы формула считала от новой точки
            y0 = y;
        }



    }


    public boolean isInField() {
        return y + height >= 0 && y <= SCR_HEIGHT;
    }

    public void draw(Batch batch) {
        int frameMultiplier = 10;
        batch.draw(framesArray[frameCounter / frameMultiplier], x, y, width, height);
        if (frameCounter++ == framesArray.length * frameMultiplier - 1) frameCounter = 0;
    }

    public void dispose() {
        for (Texture texture : framesArray) {
            texture.dispose();
        }
        jumpSound.dispose();
    }
}
