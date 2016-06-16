package com.pucpr.game.states.game.basic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
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
import com.pucpr.game.handlers.Action;
import com.pucpr.game.states.game.GameState;
import com.pucpr.game.states.game.actors.B2Object;
import com.pucpr.game.states.game.actors.Direction;
import com.pucpr.game.states.game.actors.Player;
import com.pucpr.game.states.GameScreenState;
import com.pucpr.game.states.game.actors.Knife;
import com.pucpr.game.states.game.map.utils.Util;
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
    private static final Float MAX_VELOCITY = 8f;

    private OrthographicCamera camera;
    protected AppManager manager;
    protected GameState gameState;

    /**
     * the immediate mode renderer to output our debug drawings *
     */
    private ShapeRenderer renderer;

    private SpriteBatch batch;
    private BitmapFont font;
    private Stage stage;
    private ObjectConcat playerContact;
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
     * our itens (not available on map), used to write on screen *
     */
    protected final List<B2Object> actors = new ArrayList<B2Object>();

    /**
     * Nossas armas (adicionadas na lista "actors" também). Esta lista é
     * temporária, caso o objeto complete sua ação, é removido do "mundo" box2d
     * e da tela.
     *
     */
    protected final List<Knife> weapons = new ArrayList<Knife>();
    protected final List<Body> bodiesToDestroy = new ArrayList<Body>();

    Player player;

    float updateTime;

    protected TiledMap map;
    protected MapRenderer render;

    protected Vector3 mousePos = new Vector3();

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
        loadActors();

        // register ourselfs as an InputProcessor
        Gdx.input.setInputProcessor(this);
    }

    private void createPhysicsWorld() {
        // we instantiate a new World with a proper gravity vector
        // and tell it to sleep when possible.
        world = new World(new Vector2(0, 0), true);

        // You can savely ignore the rest of this method :)
        world.setContactListener(this);
        final List<Body> bodies = Util.buildShapes(map, world, manager);

        for (Body b : bodies) {

            if (b.getUserData() != null) {
//                
                final B2Object object = (B2Object) b.getUserData();

                checkForMapConfigurations(object);

                configure((B2Object) b.getUserData(), true);
            }
        }
    }

    protected void createBoxes() {

    }

    protected B2Object create(MapObject mapObject) {
        try {

            final B2Object actor = Util.loadActor(mapObject, world, manager);

            if (actor != null) {
                if (actor instanceof Player) {
                    this.player = (Player) actor;
                }
                actors.add(actor);
            }

            return actor;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void loadActors() {

        final MapLayer mapActors = map.getLayers().get("Actors");
        if (mapActors != null) {
            for (final MapObject actorMap : mapActors.getObjects()) {

                final RectangleMapObject mapPos = (RectangleMapObject) actorMap;
                final B2Object actor = create(actorMap);

                if (actor == null) {
                    continue;
                }

                final Vector2 pos = actor.getBox2dBody().getPosition();

                pos.x = (mapPos.getRectangle().x / GameConfig.PPM);
                pos.y = (mapPos.getRectangle().y / GameConfig.PPM);

                actor.getBox2dBody().setTransform(pos, 0);

                checkForMapConfigurations(actor);
                configure(actor, false);
            }
        }
    }

    @Override
    public void render() {
        calculate();
        long start = TimeUtils.nanoTime();
        destroyDeathBodies();
        world.step(Gdx.graphics.getDeltaTime(), 8, 3);
        updateTime = (TimeUtils.nanoTime() - start) / 1000000000.0f;

        camera.update();
        mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mousePos);

        if (!GameConfig.showDebug) {
            renderApp();
        } else {
            renderDebug();
        }

    }

    @SuppressWarnings("empty-statement")
    private void renderApp() {

        final PlayerStatus status = PlayerStatus.getInstance();

        int[] baseMap = {0, 1, 2, 3};

        int[] topMap = {4};

        batch.getProjectionMatrix().set(camera.combined);
        render.setView(camera);

        render.render(baseMap);

        batch.begin();

        for (B2Object obj : actors) {

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
        renderer.end();
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
                vel.x -= 2f;
            } else if (vel.x < -maxVel) {
                vel.x += 2f;
            }

            horizontalKey = true;

        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {

            playerDirection = Direction.RIGHT;

            if (vel.x < maxVel) {
                vel.x += 2f;
            } else if (vel.x > maxVel) {
                vel.x -= 2f;
            }

            horizontalKey = true;
        }

        // apply left impulse, but only if max velocity is not reached yet
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {

            playerDirection = Direction.DOWN;

            if (vel.y > -maxVel) {
                vel.y -= 2f;
            } else if (vel.x < -maxVel) {
                vel.y += 2f;
            }
            verticalKey = true;

        } else if (Gdx.input.isKeyPressed(Input.Keys.W)) {

            playerDirection = Direction.UP;

            if (vel.y < maxVel) {
                vel.y += 2f;
            } else if (vel.y > maxVel) {
                vel.y -= 2f;
            }
            verticalKey = true;
        }

        if (!horizontalKey) {
            if (vel.x > 0) {
                if (vel.x > 2f) {
                    vel.x -= 2f;
                } else if (vel.x <= 2f) {
                    vel.x = 0f;
                }
            } else if (vel.x < 0) {
                if (vel.x <= -2f) {
                    vel.x += 2f;
                } else if (vel.x > -2f) {
                    vel.x = 0f;
                }
            }
        }

        if (!verticalKey) {
            if (vel.y > 0) {
                if (vel.y > 2f) {
                    vel.y -= 2f;
                } else if (vel.y <= 2f) {
                    vel.y = 0f;
                }
            } else if (vel.y < 0) {
                if (vel.y <= -2f) {
                    vel.y += 2f;
                } else if (vel.y > -2f) {
                    vel.y = 0f;
                }
            }
        }

        player.getBox2dBody().setLinearVelocity(vel.x, vel.y);

        calculateBullets();

        for (B2Object ob : actors) {
            ob.tick();
        }

        Collections.sort(actors, new Comparator<B2Object>() {
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
            boolean isPlayerContact = false;
            B2Object contact1 = null;

            if (obj1 instanceof Player) {
                contact1 = (B2Object) obj2;
                isPlayerContact = true;
            } else if (obj2 instanceof Player) {
                contact1 = (B2Object) obj1;
                isPlayerContact = true;
            }

            if (isPlayerContact && contact1 != null) {
                Vector2[] points = c.getWorldManifold().getPoints();
                playerContact = new ObjectConcat(contact1, points[0]);

                if (contact1 != null) {
                    contact1.getTouchAction().doAction();
                }
            } else {

                boolean isKnifeContact = false;

                if (obj1 instanceof Knife) {
                    contact1 = (B2Object) obj2;
                    isKnifeContact = true;
                } else if (obj2 instanceof Knife) {
                    contact1 = (B2Object) obj1;
                    isKnifeContact = true;
                }

                if (isKnifeContact && contact1 != null) {
                    if (contact1.isDestroyOnHit()) {
                        actors.remove(contact1);
                        bodiesToDestroy.add(contact1.getBox2dBody());
                    }
                }
            }

        }

    }

    @Override
    public void endContact(Contact contact) {
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

//        if (playerContact != null) {
//            final Conversation converstation = playerContact.contact(player);
//            if (keycode == Input.Keys.E) {
//                if (playerContact.getAction() != null) {
//                    playerContact.getAction().doAction();
//                }
//            }
//            final PlayerStatus status = PlayerStatus.getInstance();
//
//            if (converstation != null) {
//
//                if (gameState.getScreenInfo().getConversation() != null) {
//                    gameState.getScreenInfo().getConversation().abort();
        if (keycode == Input.Keys.E) {
            if (playerContact != null) {
                float dst = playerContact.pos.dst(player.getBox2dBody().getPosition());
                if (dst < 1f) {
                    final Conversation converstation = playerContact.object.contact(player);

                    if (playerContact.object.getAction() != null) {
                        playerContact.object.getAction().doAction();
                    }

                    if (converstation != null) {

                        if (gameState.getScreenInfo().getConversation() != null) {
                            gameState.getScreenInfo().getConversation().abort();
                        }

                        gameState.getScreenInfo().setConversation(converstation);
                    }
                }
            }

//                gameState.getScreenInfo().setConversation(converstation);
        } else if (keycode == Input.Keys.SPACE) {
//            Conversation converstation = gameState.getScreenInfo().getConversation();
//            if (converstation != null) {
//                converstation.next();
//            }
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
        hit();
        return true;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int newParam) {
        creatingHit = System.currentTimeMillis();
        return true;
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
        if (PlayerStatus.isKey(Keys.SWORD_TOOK) && player.getCurrentWeapon() == null && creatingHit != null) {
            startingHit = System.currentTimeMillis() - creatingHit;
            float force = startingHit > 1500 ? 1f : (startingHit.floatValue() / 1500f);
            force = force < 0.1f ? 0.1f : force;
//            force = 1;

            final Knife weapon = new Knife(BodyDef.BodyType.DynamicBody);
            weapon.init(world, manager);

            final Vector2 pos = player.getBox2dBody().getPosition();
            float angle = 0;
            final float fixPos = 1.5f;

            final Vector2 mPos = new Vector2(mousePos.x, mousePos.y);

            System.out.println("PlayerPos: " + pos + ", Mouse: " + mPos);

            final float deltaY = mPos.y - pos.y;
            final float deltaX = mPos.x - pos.x;

            final float fixAngle = MathUtils.atan2(deltaY, deltaX) * 180 / MathUtils.PI;

            System.out.println("fixAngle: " + fixAngle);
            if (Math.abs(fixAngle) > (180 - 45)) {
                pos.x -= fixPos;
//                angle = 2.3f;
            } else if (Math.abs(fixAngle) < 45) {
                pos.x += fixPos;
//                angle = 1.7f;
//                force = -force;
            } else if (fixAngle > 45 && fixAngle < 135) {
                pos.y += fixPos;
//                angle = 1.3f;
            } else if (fixAngle < -45 && fixAngle > -135) {
                pos.y -= fixPos;
//                angle = 3.3f;
            }

//            
            force = force * 120;
//            Double angleFixed = fixAngle * 180 / Math.PI % 360;
            final float angle360 = (fixAngle + 180) - 90;
            weapon.setPos(pos, angle360 * MathUtils.degreesToRadians);

            weapon.getBox2dBody().setLinearVelocity(new Vector2(force * MathUtils.cosDeg(fixAngle), force * MathUtils.sinDeg(fixAngle)));

            weapon.setRotation(angle360);
            weapon.setStartHitAngle(angle);

            actors.add(weapon);
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

        final List<Knife> toRemove = new ArrayList();

        for (Knife a : weapons) {
            if (a.isComplete()) {
                toRemove.add(a);
            }
        }

        actors.removeAll(toRemove);
        weapons.removeAll(toRemove);

        for (Knife a : toRemove) {
            world.destroyBody(a.getBox2dBody());
            if (player.getCurrentWeapon() != null && player.getCurrentWeapon() == a) {
                player.setCurrentWeapon(null);
            }
        }
    }

    protected void configure(B2Object actor, boolean block) {
    }

    private void checkForMapConfigurations(final B2Object object) {
        if (object.getProperies() != null) {

            final String event = object.getProperies().get("event", String.class);
            final String nextScreen = object.getProperies().get("NextScreen", String.class);
            final String mustHaveKeys = object.getProperies().get("MustHaveKeys", String.class);
            final String addKeys = object.getProperies().get("AddKeys", String.class);
            final String destroyObjects = object.getProperies().get("DestroyObjects", String.class);
            final String[] mustHaveKeysArr = mustHaveKeys == null ? new String[]{} : mustHaveKeys.split(",");
            final String[] addKeysArr = addKeys == null ? new String[]{} : addKeys.split(",");
            final String[] destroyObjectsArr = destroyObjects == null ? new String[]{} : destroyObjects.split(",");
            final String detroyOnEvent = object.getProperies().get("DestroyOnEvent", String.class);
            final String playGetItemSound = object.getProperies().get("PlayGetItemSound", String.class);
            final String destroyOnHit = object.getProperies().get("DestroyOnHit", String.class);

            final Action acc = new Action() {
                @Override
                public void doAction() {
                    for (String keyStr : mustHaveKeysArr) {
                        final Keys key = Keys.valueOf(keyStr.trim());
                        if (!PlayerStatus.isKey(key)) {
                            System.out.println("Player must have key: " + key.name() + " to interact with this object!");
                            return;
                        }
                    }

                    for (String keyStr : addKeysArr) {
                        final Keys key = Keys.valueOf(keyStr.trim());
                        PlayerStatus.getInstance().set(key, true);
                    }

                    if (nextScreen != null && !nextScreen.isEmpty()) {
                        final BasicGameScreen screen = Util.loadScreen(nextScreen);
                        gameState.setScreen(screen);
                    }

                    if (detroyOnEvent != null && detroyOnEvent.equalsIgnoreCase("true")) {
                        actors.remove(object);
                        world.destroyBody(object.getBox2dBody());
                    }

                    // Login will remove some other actors?
                    final List<B2Object> toRemove = new ArrayList();

                    for (String k : destroyObjectsArr) {
                        for (B2Object a : actors) {
                            if (a.getName() != null && a.getName().equals(k)) {
                                toRemove.add(a);
                            }
                        }
                    }

                    for (B2Object b : toRemove) {
                        world.destroyBody(b.getBox2dBody());
                    }

                    if (playGetItemSound != null && playGetItemSound.equalsIgnoreCase("true")) {
                        GameConfig.SOUND_MANAGER.playGetItemSound();
                    }

                    actors.removeAll(toRemove);
                    weapons.removeAll(toRemove);

                }
            };

            if (event == null || event.equals("action")) {
                object.addAction(acc);
            } else if (event.equals("touch")) {
                object.addTouchAction(acc);
            }

            object.setDestroyOnHit(destroyOnHit != null && destroyOnHit.equalsIgnoreCase("true"));
        }
    }

    private void destroyDeathBodies() {
        
        for (Body b : bodiesToDestroy) {
            world.destroyBody(b);
        }
        
        bodiesToDestroy.clear();
    }

    private static class ObjectConcat {

        public ObjectConcat(B2Object object, Vector2 pos) {
            this.object = object;
            this.pos = pos;
        }

        private B2Object object;
        private Vector2 pos;

    }
}
