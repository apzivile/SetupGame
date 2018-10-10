package lt.baltictalents.nullapointershooter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class ShooterGame extends ApplicationAdapter {
    public static final int VIEWPORT_WIDTH = 720;
    public static final int VIEWPORT_HEIGHT = 1280;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Texture background;
    private AnimatedSprite spaceshipAnimated;
    private ShotManager shotManager;

    @Override
    public void create() {

        camera = new OrthographicCamera();
        camera.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        batch = new SpriteBatch();

        background = new Texture(Gdx.files.internal("space_background_v4.png"));

        Music music = Gdx.audio.newMusic(Gdx.files.internal("background_music.mp3"));
        music.setVolume(.25f);
        music.setLooping(true);
        music.play();

        Texture spaceshipTexture = new Texture(Gdx.files.internal("ship_spritesheet.png"));
        Sprite spaceshipSprite = new Sprite(spaceshipTexture);
        spaceshipAnimated = new AnimatedSprite(spaceshipSprite);
        spaceshipAnimated.setPosition(VIEWPORT_WIDTH / 2, 0);

        Texture shotTexture = new Texture(Gdx.files.internal("ship_shots_spritesheet.png"));
        shotManager = new ShotManager(shotTexture);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(background, 0, 0);
        spaceshipAnimated.draw(batch);
        shotManager.draw(batch);
        batch.end();
        handleInput();
        spaceshipAnimated.move();
        shotManager.update();

    }

    private void handleInput() {
        if (Gdx.input.isTouched()) {
            Vector3 touchPosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPosition);

            if (touchPosition.x > spaceshipAnimated.getX()) {
                spaceshipAnimated.moveRight();
            } else {
                spaceshipAnimated.moveLeft();
            }
            shotManager.firePlayerShot(spaceshipAnimated.getX());

        }
    }

    @Override
    public void dispose() {
        batch.dispose();

    }
}
