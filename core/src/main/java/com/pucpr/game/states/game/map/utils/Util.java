/**
 * MapBox2dUtil.class
 */
package com.pucpr.game.states.game.map.utils;

import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.pucpr.game.AppManager;
import com.pucpr.game.GameConfig;
import com.pucpr.game.states.game.actors.B2Object;
import com.pucpr.game.states.game.basic.BasicGameScreen;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Luis Boch
 * @email luis.c.boch@gmail.com
 * @since May 22, 2016
 */
public class Util {

    private static float ppt;

    public static List<Body> buildShapes(Map map, World world, AppManager manager) {
        ppt = GameConfig.PPM;
        MapObjects objects = map.getLayers().get("Box2dLayer").getObjects();

        final List<Body> bodies = new ArrayList<Body>();

        for (MapObject object : objects) {

            if (object instanceof TextureMapObject) {
                continue;
            }

            Shape shape;

            if (object instanceof RectangleMapObject) {
                shape = getRectangle((RectangleMapObject) object);
            } else if (object instanceof PolygonMapObject) {
                shape = getPolygon((PolygonMapObject) object);
            } else if (object instanceof PolylineMapObject) {
                shape = getPolyline((PolylineMapObject) object);
            } else if (object instanceof CircleMapObject) {
                shape = getCircle((CircleMapObject) object);
            } else if (object instanceof TiledMapTileMapObject) {
                shape = getCircle((CircleMapObject) object);
            } else {
                System.out.println("Can't build build object type: " + object.getClass().getCanonicalName());
                continue;
            }

            BodyDef bd = new BodyDef();
            bd.type = BodyType.StaticBody;
            Body body = world.createBody(bd);
            body.createFixture(shape, 1);

            bodies.add(body);

            shape.dispose();

            if (object.getName() != null && !object.getName().isEmpty()) {
                final B2Object actor = loadActor(object, world, manager);
                body.setUserData(actor);
                actor.setBox2dBody(body);
            }
        }

        return bodies;

    }

    public static <E extends B2Object> E loadActor(MapObject actorName, World world, AppManager manager) {
        final E actor = loadActor(actorName.getName(), world, manager);
        if (actor != null) {
            actor.setProperies(actorName.getProperties());
        }
        return actor;
    }

    public static <E extends B2Object> E loadActor(String actorName, World world, AppManager manager) {
        if (actorName == null) {
            return null;
        }
        try {
            final Class<E> actorClass = (Class<E>) Class.forName("com.pucpr.game.states.game.actors." + actorName);
            final E actor = actorClass.newInstance();
            actor.init(world, manager);
            return actor;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static <E extends BasicGameScreen> E loadScreen(String screenName) {

        try {
            final Class<E> screenClass = (Class<E>) Class.forName("com.pucpr.game.states.game.locations." + screenName);
            final E screen = screenClass.newInstance();
            return screen;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private static PolygonShape getRectangle(RectangleMapObject rectangleObject) {
        Rectangle rectangle = rectangleObject.getRectangle();
        PolygonShape polygon = new PolygonShape();
        Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5f) / ppt,
                (rectangle.y + rectangle.height * 0.5f) / ppt);
        polygon.setAsBox(rectangle.width * 0.5f / ppt,
                rectangle.height * 0.5f / ppt,
                size,
                0.0f);
        return polygon;
    }

    private static CircleShape getCircle(CircleMapObject circleObject) {
        Circle circle = circleObject.getCircle();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(circle.radius / ppt);
        circleShape.setPosition(new Vector2(circle.x / ppt, circle.y / ppt));
        return circleShape;
    }

    private static PolygonShape getPolygon(PolygonMapObject polygonObject) {
        PolygonShape polygon = new PolygonShape();
        float[] vertices = polygonObject.getPolygon().getTransformedVertices();

        float[] worldVertices = new float[vertices.length];

        for (int i = 0; i < vertices.length; ++i) {
            worldVertices[i] = vertices[i] / ppt;
        }

        polygon.set(worldVertices);
        return polygon;
    }

    private static ChainShape getPolyline(PolylineMapObject polylineObject) {
        float[] vertices = polylineObject.getPolyline().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for (int i = 0; i < vertices.length / 2; ++i) {
            worldVertices[i] = new Vector2();
            worldVertices[i].x = vertices[i * 2] / ppt;
            worldVertices[i].y = vertices[i * 2 + 1] / ppt;
        }

        ChainShape chain = new ChainShape();
        chain.createChain(worldVertices);
        return chain;
    }
}
