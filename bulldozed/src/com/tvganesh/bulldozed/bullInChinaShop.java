package com.tvganesh.bulldozed;


import java.util.Vector;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.debug.Debug;

import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;


public class bullInChinaShop extends SimpleBaseGameActivity implements IAccelerationListener, IOnSceneTouchListener {
	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;
	private static final float  DEGTORAD = 0.0174532925199432957f;
	public static final float PIXEL_TO_METER_RATIO_DEFAULT = 32.0f;
	
	private BitmapTextureAtlas mBitmapTextureAtlas;
	
	
	private ITextureRegion mTumblerTextureRegion;
	private ITextureRegion mGlassTextureRegion;
	private ITextureRegion mBottleTextureRegion;
	private ITextureRegion mVaseTextureRegion;
	private ITextureRegion mWineTextureRegion;
	private ITextureRegion mPlateTextureRegion;
	private ITextureRegion mWineGlassTextureRegion;
	private ITextureRegion mBullTextureRegion;
	
    private ITextureRegion mTexture;
    
    private Scene mScene;
    
    private PhysicsWorld mPhysicsWorld;
	
    
    private static FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(50f, 0.0f, 0.5f);
	public EngineOptions onCreateEngineOptions() {
		
		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
	}
	
	public void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 556, 246, TextureOptions.BILINEAR);
		
		this.mTumblerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "tumblr.png", 0, 0);
		this.mBitmapTextureAtlas.load();
		
		this.mBottleTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "bottle.png",20, 29);
		this.mBitmapTextureAtlas.load();		
		
		this.mGlassTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "glass.png",36, 69);
		this.mBitmapTextureAtlas.load();
		

		
		this.mVaseTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "vase.png",56, 89);
		this.mBitmapTextureAtlas.load();
		
		this.mWineTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "wine.png",76, 127);
		this.mBitmapTextureAtlas.load();
		
		this.mWineGlassTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "wineglass.png",96, 171);
		this.mBitmapTextureAtlas.load();
		
		this.mPlateTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "plate.png",116, 208);
		this.mBitmapTextureAtlas.load();
		
		this.mBullTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "bison.png",516, 218);
		this.mBitmapTextureAtlas.load();
	}
	
	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		this.mScene = new Scene();
		this.mScene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));
		
		//this.mPhysicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);
		this.mPhysicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_MOON), false);
		//this.mPhysicsWorld = new PhysicsWorld(new Vector2(0, 0), false);
		
		this.mScene.setOnSceneTouchListener(this);
		this.initChinaStack(mScene);
		this.mScene.registerUpdateHandler(this.mPhysicsWorld);

		return mScene;		
		
	}
	
	public void initChinaStack(Scene mScene){
		
		Sprite tumbler, glass,vase,wine,bottle,wineglass,plate;
		Body tumblerBody, glassBody,vaseBody,wineBody,bottleBody,wineglassBody,plateBody;
		
		
		
		
		// Create a Physics World
		//this.mPhysicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);

		
		//Create the floor		
		final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();
		final Rectangle ground = new Rectangle(0, CAMERA_HEIGHT - 2, CAMERA_WIDTH, 2, vertexBufferObjectManager);
		final Rectangle roof = new Rectangle(0, 0, CAMERA_WIDTH, 2, vertexBufferObjectManager);
		final Rectangle left = new Rectangle(0, 0, 2, CAMERA_HEIGHT, vertexBufferObjectManager);
		final Rectangle right = new Rectangle(CAMERA_WIDTH - 2, 0, 2, CAMERA_HEIGHT, vertexBufferObjectManager);

		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.0f, 0.5f);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, ground, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, roof, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, left, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, right, BodyType.StaticBody, wallFixtureDef);

		this.mScene.attachChild(ground);
		this.mScene.attachChild(roof);
		this.mScene.attachChild(left);
		this.mScene.attachChild(right);

		// Add tumblers
		for(int i=0; i < 21; i++) {
			
			  tumbler = new Sprite(80 + i * 25, 450, this.mTumblerTextureRegion, this.getVertexBufferObjectManager());	
			  FIXTURE_DEF = PhysicsFactory.createFixtureDef(1f, 0.0f, 1f);
			  tumblerBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld, tumbler, BodyType.DynamicBody, FIXTURE_DEF);	  
			  
			  this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(tumbler, tumblerBody, true, true));
			  this.mScene.attachChild(tumbler);	
			  
			
		}
			
		
		// Add glasses
		for(int i=0; i < 14; i++) {
			
			  glass = new Sprite(130 + i * 25, 428, this.mGlassTextureRegion, this.getVertexBufferObjectManager());	
			  FIXTURE_DEF = PhysicsFactory.createFixtureDef(1f, 0.0f, 1f);
			  glassBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld, glass, BodyType.DynamicBody, FIXTURE_DEF);	  
			
			  this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(glass, glassBody, true, true));
			  this.mScene.attachChild(glass);	
			 
			
		}
		
		// Add plate	
		  plate = new Sprite(125 , 416, this.mPlateTextureRegion, this.getVertexBufferObjectManager());			
		  FIXTURE_DEF = PhysicsFactory.createFixtureDef(1f, 0.0f, 1f);
		  plateBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld, plate, BodyType.DynamicBody, FIXTURE_DEF);	  
		  
		  this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(plate, plateBody, true, true));
		  this.mScene.attachChild(plate);	
	
		
		// Add wine
		for(int i=0; i < 10; i++) {
			
			  wine = new Sprite(150 + i * 30, 382, this.mWineTextureRegion, this.getVertexBufferObjectManager());			
			  wineBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld, wine, BodyType.DynamicBody, FIXTURE_DEF);	  
			  
			  this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(wine, wineBody, true, true));
			  this.mScene.attachChild(wine);		
		}	
		
		// Add plate	
		  plate = new Sprite(125 , 372, this.mPlateTextureRegion, this.getVertexBufferObjectManager());			
		  FIXTURE_DEF = PhysicsFactory.createFixtureDef(1f, 0.0f, 1f);
		  plateBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld, plate, BodyType.DynamicBody, FIXTURE_DEF);	  
		  
		  this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(plate, plateBody, true, true));
		  this.mScene.attachChild(plate);	
		 
		// Add bottle
		for(int i=0; i < 8; i++) {
			
			  bottle = new Sprite(200 + i * 25, 330, this.mBottleTextureRegion, this.getVertexBufferObjectManager());			
			  bottleBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld, bottle, BodyType.DynamicBody, FIXTURE_DEF);	  
			  
			  this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(bottle, bottleBody, true, true));
			  this.mScene.attachChild(bottle);		
		}	
		// Add plate	
		  plate = new Sprite(125 , 318, this.mPlateTextureRegion, this.getVertexBufferObjectManager());			
		  FIXTURE_DEF = PhysicsFactory.createFixtureDef(1f, 0.0f, 1f);
		  plateBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld, plate, BodyType.DynamicBody, FIXTURE_DEF);	  
		  
		  this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(plate, plateBody, true, true));
		  this.mScene.attachChild(plate);
		// Add wine glass
		for(int i=0; i < 4; i++) {
			
			  wineglass = new Sprite(250 + i * 25, 280, this.mWineGlassTextureRegion, this.getVertexBufferObjectManager());			
			  wineglassBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld, wineglass, BodyType.DynamicBody, FIXTURE_DEF);	  
			  
			  this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(wineglass, wineglassBody, true, true));
			  this.mScene.attachChild(wineglass);		
		}	
		
		  vase = new Sprite(300 , 241, this.mVaseTextureRegion, this.getVertexBufferObjectManager());			
		  FIXTURE_DEF = PhysicsFactory.createFixtureDef(1f, 0.0f, 1f);
		  vaseBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld, vase, BodyType.DynamicBody, FIXTURE_DEF);	  
		  
		  this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(vase, vaseBody, true, true));
		  this.mScene.attachChild(vase);		
		
		this.mScene.registerUpdateHandler(this.mPhysicsWorld);
	}


	@Override
	public void onAccelerationAccuracyChanged(AccelerationData pAccelerationData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAccelerationChanged(AccelerationData pAccelerationData) {
		final Vector2 gravity = Vector2Pool.obtain(pAccelerationData.getX(), pAccelerationData.getY());
		this.mPhysicsWorld.setGravity(gravity);
		Vector2Pool.recycle(gravity);
		
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
			if(this.mPhysicsWorld != null) {
				if(pSceneTouchEvent.isActionDown()) {
					this.addBull(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
					return true;
				}
			}
			return false;
	}
	
	private void addBull(final float pX, final float pY) {
		
	

		final Sprite bull;
		final Body bullBody;

		
		bull = new Sprite(pX, pY, this.mBullTextureRegion, this.getVertexBufferObjectManager());
		FIXTURE_DEF = PhysicsFactory.createFixtureDef(25f, 0.0f, 1f);
		Log.d("here","here");
		bullBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld, bull, BodyType.DynamicBody, FIXTURE_DEF);
		if(pX > 360)
		      bullBody.setLinearVelocity(-5,-5);
		else
			bullBody.setLinearVelocity(5,5);
		
		this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(bull, bullBody, true, true));
		this.mScene.attachChild(bull);		
		
		this.mScene.registerUpdateHandler(this.mPhysicsWorld);
	}
	
	@Override
	public void onResumeGame() {
		super.onResumeGame();

		
	}

	@Override
	public void onPauseGame() {
		super.onPauseGame();

		
	}
	

}