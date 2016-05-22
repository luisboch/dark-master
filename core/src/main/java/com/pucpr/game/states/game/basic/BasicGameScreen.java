package com.pucpr.game.states.game.basic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.TimeUtils;
import com.pucpr.game.AppManager;
import com.pucpr.game.GameConfig;
import com.pucpr.game.Keys;
import com.pucpr.game.PlayerStatus;
import com.pucpr.game.states.game.GameState;
import com.pucpr.game.states.game.b2d.objects.B2Object;
import com.pucpr.game.states.game.b2d.objects.Direction;
import com.pucpr.game.states.game.b2d.objects.Player;
import com.pucpr.game.states.GameScreenState;
import com.pucpr.game.states.game.b2d.objects.Weapon;
import com.pucpr.game.states.game.map.utils.MapBox2dUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author luis
 */
public class BasicGameScreen implements GameScreenState, InputProcessor, ContactListener {

    private Direction playerDirection = Direction.DOWN;
    private static final Float MAX_VELOCITY = 16f;

    private OrthographicCamera camera;
    protected AppManager manager;
    private GameState gameState;

    /**
     * the immediate mode renderer to output our debug drawings *
     */
    private ShapeRenderer renderer;

    private SpriteBatch batch;
    private BitmapFont font;
    private Stage stage;
    private B2Object playerContact;
    private Long creatingHit = null;
    private Long startingHit = null;

    /**
     * box2d debug renderer *
     */
    protected Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();

    /**
     * our box2D world *
     */
    protected World world;

    /**
     * our itens (used to write on screen) *
     */
    protected final List<B2Object> objects = new ArrayList();

    /**
     * Nossas armas (adicionadas na lista "objects" também). Esta lista é
     * temporária, caso o objeto complete sua ação, é removido do "mundo" box2d
     * e da tela.
     *
     */
    protected final List<Weapon> weapons = new ArrayList();

    /**
     * our ground box *
     */
    Body groundBody;

    /**
     * a hit body *
     */
    Body hitBody = null;

    Player player;

    float updateTime;

    protected TiledMap map;
    protected MapRenderer render;

    public BasicGameScreen(final String mapFile) {
        map = new TmxMapLoader().load("data/images/sprites/maps/" + mapFile);
    }

    @Override
    public void create() {
        // setup the camera. In Box2D we operate on a
        // meter scale, pixels won't do it. So we use
        // an orthographic camera with a viewport of
        // 48 meters in width and 28 meters in height.
        // We also position the camera so that it
        // looks at (0,16) (that's where the middle of the
        // screen will be located).
        camera = new OrthographicCamera(Gdx.graphics.getWidth() / GameConfig.PPM, Gdx.graphics.getHeight() / GameConfig.PPM);
        camera.setToOrtho(false, 30, 20);
        camera.position.x = 0;
        camera.position.y = 100;

        render = new OrthogonalTiledMapRenderer(map, 1f / GameConfig.PPM);
        // next we setup the immediate mode renderer
        renderer = new ShapeRenderer();

        // next we create a SpriteBatch and a font
        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("data/arial-15.fnt"), false);
        font.setColor(Color.RED);

        // next we create out physics world.
        createPhysicsWorld();
        createBoxes();
        createPlayer(world);

        // register ourselfs as an InputProcessor
        Gdx.input.setInputProcessor(this);
    }

    private void createPhysicsWorld() {
        // we instantiate a new World with a proper gravity vector
        // and tell it to sleep when possible.
        world = new World(new Vector2(0, 0), true);

        // You can savely ignore the rest of this method :)
        world.setContactListener(this);
        this.objects.addAll(MapBox2dUtil.buildShapes(map, world, manager));
    }

    protected void createBoxes() {

    }

    public void createPlayer(World world) {
        final Player pl = new Player(world, manager);
        final Vector2 pos = pl.getBox2dBody().getPosition();
        try {
            RectangleMapObject mapPos = (RectangleMapObject) map.getLayers().get("PlayerPos").getObjects().get("PlayerPos");

            pos.x = mapPos.getRectangle().x / GameConfig.PPM;
            pos.y = mapPos.getRectangle().y / GameConfig.PPM;
        } catch (Exception ex) {
            System.out.println(
                    "Failed to load player position... There are an Layer with name \"PlayerPos\" and Object with Name \"PlayerPos\"? ");
            pos.x = 0;
            pos.y = 100;
        }

        pl.getBox2dBody().setTransform(pos, 0);

        this.player = pl;
        this.objects.add(pl);
    }

    @Override
    public void render() {
        calculate();
        long start = TimeUtils.nanoTime();
        world.step(Gdx.graphics.getDeltaTime(), 8, 3);
        updateTime = (TimeUtils.nanoTime() - start) / 1000000000.0f;

        camera.update();

        if (!GameConfig.showDebug) {
            renderApp();
        } else {
            renderDebug();
        }

    }

    private void renderApp() {

//        renderer.setProjectionMatrix(camera.combined);
        int[] baseMap = {0, 1, 2, 3};
        int[] topMap = {4};

        batch.getProjectionMatrix().set(camera.combined);
        render.setView(camera);

        render.render(baseMap);
//        
        batch.begin();

        for (B2Object obj : objects) {

            final Body box = obj.getBox2dBody();

            Vector2 position = box.getPosition(); // that's the box's center position

            float angle = obj.getAngle() != null ? MathUtils.radiansToDegrees * obj.getAngle() : 0;
            final TextureRegion texture = obj.getTextureRegion();

            if (texture != null) {
                batch.draw(texture, position.x - 1, position.y - 1, // the bottom left corner of the box, unrotated
                        1f, 1f, // the rotation center relative to the bottom left corner of the box
                        2, 2, // the width and height of the box
                        obj.getScale(), obj.getScale(), // the scale on the x- and y-axis
                        angle); // the rotation angle
            }

        }
//
        batch.end();
        render.render(topMap);

//        writeFPS();
    }

    private void renderDebug() {

        debugRenderer.render(world, camera.combined);

        renderer.begin(ShapeType.Point);

        renderer.setColor(0, 1, 0, 1);

        for (int i = 0; i < world.getContactCount(); i++) {
            Contact contact = world.getContactList().get(i);
            // we only render the contact if it actually touches
            if (contact.isTouching()) {
                // get the world manifold from which we get the
                // contact points. A manifold can have 0, 1 or 2
                // contact points.
                WorldManifold manifold = contact.getWorldManifold();
                int numContactPoints = manifold.getNumberOfContactPoints();
                for (int j = 0; j < numContactPoints; j++) {
                    Vector2 point = manifold.getPoints()[j];
                    renderer.point(point.x, point.y, 0);
                }
            }
        }
    }

    private void calculate() {
        // cap max velocity on x
        final Vector2 vel = player.getBox2dBody().getLinearVelocity();

        if (Math.abs(vel.x) > MAX_VELOCITY) {
            vel.x = Math.signum(vel.x) * MAX_VELOCITY;
            player.getBox2dBody().setLinearVelocity(vel.x, vel.y);
        }

        float maxVel = MAX_VELOCITY / 2;

        // apply left impulse, but only if max velocity is not reached yet
        if (Gdx.input.isKeyPressed(Input.Keys.A)
                || Gdx.input.isKeyPressed(Input.Keys.W)
                || Gdx.input.isKeyPressed(Input.Keys.S)
                || Gdx.input.isKeyPressed(Input.Keys.D)) {

            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                GameConfig.SOUND_MANAGER.setRunning(true);
                player.setRunning(true);
                maxVel = MAX_VELOCITY;
            } else {
                GameConfig.SOUND_MANAGER.setWalking(true);
                player.setRunning(true);
            }

        } else if (vel.y == 0 && vel.x == 0) {
            GameConfig.SOUND_MANAGER.setRunning(false);
            player.setRunning(false);
            player.setWalking(false);
            GameConfig.SOUND_MANAGER.setWalking(false);
        }

        boolean horizontalKey = false;
        boolean verticalKey = false;

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            playerDirection = Direction.LEFT;
            if (vel.x > -maxVel) {
                player.getBox2dBody().setLinearVelocity(vel.x - 2f, vel.y);
            } else if (vel.x < -maxVel) {
                player.getBox2dBody().setLinearVelocity(vel.x + 2f, vel.y);
            }

            horizontalKey = true;

        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {

            playerDirection = Direction.RIGHT;

            if (vel.x < maxVel) {
                player.getBox2dBody().setLinearVelocity(vel.x + 2f, vel.y);
            } else if (vel.x > maxVel) {
                player.getBox2dBody().setLinearVelocity(vel.x - 2f, vel.y);
            }

            horizontalKey = true;
        }

        // apply left impulse, but only if max velocity is not reached yet
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {

            playerDirection = Direction.DOWN;

            if (vel.y > -maxVel) {
                player.getBox2dBody().setLinearVelocity(vel.x, vel.y - 2f);
            } else if (vel.x < -maxVel) {
                player.getBox2dBody().setLinearVelocity(vel.x, vel.y + 2f);
            }
            verticalKey = true;

        } else if (Gdx.input.isKeyPressed(Input.Keys.W)) {

            playerDirection = Direction.UP;

            if (vel.y < maxVel) {
                player.getBox2dBody().setLinearVelocity(vel.x, vel.y + 2f);
            } else if (vel.y > maxVel) {
                player.getBox2dBody().setLinearVelocity(vel.x, vel.y - 2f);
            }
            verticalKey = true;
        }

        if (!horizontalKey) {
            if (vel.x > 0) {
                if (vel.x > 2f) {
                    player.getBox2dBody().setLinearVelocity(vel.x - 2f, vel.y);
                } else if (vel.x <= 2f) {
                    player.getBox2dBody().setLinearVelocity(0, vel.y);
                }
            } else if (vel.x < 0) {
                if (vel.x <= -2f) {
                    player.getBox2dBody().setLinearVelocity(vel.x + 2f, vel.y);
                } else if (vel.x > -2f) {
                    player.getBox2dBody().setLinearVelocity(0, vel.y);
                }
            }
        }

        if (!verticalKey) {
            if (vel.y > 0) {
                if (vel.y > 2f) {
                    player.getBox2dBody().setLinearVelocity(vel.x, vel.y - 2f);
                } else if (vel.y <= 2f) {
                    player.getBox2dBody().setLinearVelocity(vel.x, 0);
                }
            } else if (vel.y < 0) {
                if (vel.y <= -2f) {
                    player.getBox2dBody().setLinearVelocity(vel.x, vel.y + 2f);
                } else if (vel.y > -2f) {
                    player.getBox2dBody().setLinearVelocity(vel.x, 0);
                }
            }
        }

        calculateBullets();

        for (B2Object ob : objects) {
            ob.tick();
        }

        Collections.sort(objects, new Comparator<B2Object>() {
            @Override
            public int compare(B2Object o1, B2Object o2) {
                if (o1.getBox2dBody() == null || o2.getBox2dBody() == null) {
                    return 0;
                } else if (o1.getBox2dBody().getPosition().y > o2.getBox2dBody().getPosition().y) {
                    return -1;
                } else if (o1.getBox2dBody().getPosition().y < o2.getBox2dBody().getPosition().y) {
                    return 1;
                }
                return 0;
            }
        });

        player.setDirection(playerDirection);
        camera.position.set(player.getBox2dBody().getPosition().x, player.getBox2dBody().getPosition().y, 0);

        camera.update();
    }

    @Override
    public void beginContact(Contact c) {
        final Object obj1 = c.getFixtureA().getBody().getUserData();
        final Object obj2 = c.getFixtureB().getBody().getUserData();
        if (obj1 != null && obj2 != null) {
            final B2Object contact;
            if (obj1 instanceof Player) {
                contact = (B2Object) obj2;
            } else if (obj2 instanceof Player) {
                contact = (B2Object) obj1;
            } else {
                contact = null;
            }

            playerContact = contact;
        }

    }

    @Override
    public void endContact(Contact contact) {
        playerContact = null;
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public void setManager(AppManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean keyDown(int keycode) {

        if (keycode == Input.Keys.SPACE) {
            creatingHit = System.currentTimeMillis();
        }

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.ENTER) {
            if (playerContact != null) {
                final Conversation converstation = playerContact.contact(player);

                if (playerContact.getAction() != null) {
                    playerContact.getAction().doAction();
                }

                if (converstation != null) {

                    if (gameState.getScreenInfo().getConversation() != null) {
                        gameState.getScreenInfo().getConversation().abort();
                    }

                    gameState.getScreenInfo().setConversation(converstation);
                }
            }
        } else if (keycode == Input.Keys.SPACE) {
            hit();
        }

        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        return true;
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer) {

        return false;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int newParam) {

        return false;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public BitmapFont getFont() {
        return font;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void writeFPS() {
        batch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.begin();

        font.draw(batch, "fps: " + Gdx.graphics.getFramesPerSecond() + " update time: " + updateTime, 0, 10);

        batch.end();
    }

    private void hit() {
        if (PlayerStatus.isKey(Keys.SWORD_TAKED) && player.getCurrentWeapon() == null) {
            startingHit = System.currentTimeMillis() - creatingHit;
            float force = startingHit > 1500 ? 1f : (startingHit.floatValue() / 1500f);
            force = force < 1 ? 0.1f : force;

            System.out.println("HIT! WITH " + force + " of power");

            final Weapon weapon = new Weapon(world, manager);

            final Vector2 pos = player.getBox2dBody().getPosition();
            float angle = 0;
            final float fixPos = 1f;

            if (player.getDirection() == Direction.LEFT) {
                pos.x -= fixPos;
                angle = 2.3f;
            } else if (player.getDirection() == Direction.RIGHT) {
                pos.x += fixPos;
                angle = 1.7f;
                force = -force;
            } else if (player.getDirection() == Direction.UP) {
                pos.y += fixPos;
                angle = 1.3f;
            } else if (player.getDirection() == Direction.DOWN) {
                pos.y -= fixPos;
                angle = 3.3f;
            }

            final float startAngle = angle * 90 * MathUtils.degRad;

            weapon.setPos(pos, startAngle);
            weapon.getBox2dBody().applyAngularImpulse(50 * force, true);
            weapon.setStartHitAngle(startAngle);

            objects.add(weapon);
            weapons.add(weapon);
            player.setCurrentWeapon(weapon);
//            final FrictionJointDef def = new FrictionJointDef();
//            def.bodyA = player.getBox2dBody();
//            def.bodyB = weapon.getBox2dBody();
//            def.type = JointDef.JointType.FrictionJoint;
//            def.
//            world.createJoint(def);
        }
    }

    private void calculateBullets() {

        final List<Weapon> toRemove = new ArrayList();

        for (Weapon a : weapons) {
            if (a.isComplete()) {
                toRemove.add(a);
            }
        }

        objects.removeAll(toRemove);
        weapons.removeAll(toRemove);

        for (Weapon a : toRemove) {
            world.destroyBody(a.getBox2dBody());
            if (player.getCurrentWeapon() != null && player.getCurrentWeapon() == a) {
                player.setCurrentWeapon(null);
            }
        }
    }
}
