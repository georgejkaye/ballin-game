package physics;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import resources.Map;
import resources.Map.Tile;
import resources.Resources;
import resources.Resources.Mode;
import resources.Character;
import resources.Character.Class;
import resources.Character.Heading;

public class PhysicsTests {
	private Physics physics;
	private Resources r;
	private Map map;
	private boolean verbose = false;
	private static final ActionEvent ev = new ActionEvent(new Object(), 0, "");
	
	@Before
	public void setUp() {
		r = new Resources();
		physics = new Physics(r, false);
		r.mode = Mode.LastManStanding;
		map = new Map(
				new Point2D.Double(0,0), // origin
				100, // width
				100, // height
				0.0, // friction (no friction! Hahahahahaaa!)
				"TestMap");
		
		r.setMap(map);
	}

	@After
	public void tearDown() {
		physics = null;
		r.clearPlayerList();
	}

	@Test
	public void testActionPerformed1() {
		//Test physics.actionPerformed().
		//Test single character movement:
		// starts character in the middle of the map, then moves them in each direction for twenty steps.
		Character c1 = new Character(1, 50, 50, 25, Heading.STILL, Class.WARRIOR, 0, "Player 1");
		r.addPlayerToList(c1);
		double acc = c1.getAcc();
		double maxdx = c1.getMaxDx(), maxdy = c1.getMaxDy();
		double dx = c1.getDx(), dy = c1.getDy();
		double x = c1.getX(), y = c1.getY();
		for(Character.Heading h : new Character.Heading[]{Character.Heading.N, Character.Heading.NW, Character.Heading.NE, Character.Heading.W, Character.Heading.E, Character.Heading.S, Character.Heading.SW, Character.Heading.SE, Character.Heading.STILL}) {
			switch(h) {
			case N:
				c1.setControls(true, false, false, false, false, false);
				break;
			case E:
				c1.setControls(false, false, false, true, false, false);
				break;
			case NE:
				c1.setControls(true, false, false, true, false, false);
				break;
			case NW:
				c1.setControls(true, false, true, false, false, false);
				break;
			case S:
				c1.setControls(false, true, false, false, false, false);
				break;
			case SE:
				c1.setControls(false, true, false, true, false, false);
				break;
			case STILL:
				c1.setControls(false, false, false, false, false, false);
				break;
			case SW:
				c1.setControls(false, true, true, false, false, false);
				break;
			case W:
				c1.setControls(false, false, true, false, false, false);
				break;
			}
			dx = 0.0;
			dy = 0.0;
			c1.setDx(0);
			c1.setDy(0);
			c1.setX(50.0);
			c1.setY(50.0);
			for(int step = 1; step <= 20; step++) {
				//20 steps
				x = c1.getX();
				y = c1.getY();
				if( c1.isLeft()  ) dx = Math.max(-maxdx, dx - acc);
				if( c1.isRight() ) dx = Math.min( maxdx, dx + acc);
				if( c1.isUp()    ) dy = Math.max(-maxdy, dy - acc);
				if( c1.isDown()  ) dy = Math.min( maxdy, dy + acc);
				
				physics.actionPerformed(ev);
				if(verbose) System.out.println(h + " step " + step + ": dx " + dx + ", dy " + dy + ", coords: (" + x + "," + y + ")");
				assertTrue("Error somewhere in actionPerformed.",Double.compare(c1.getX(), x + dx) == 0);
				assertTrue("Error somewhere in actionPerformed.",Double.compare(c1.getY(), y + dy) == 0);
			}
		}
		
	}
	
	@Test
	public void testActionPerformed2() {
		//Test physics.actionPerformed().
		//Test two character collision:
		fail("Not yet implemented");
	}
	
	@Test
	public void testActionPerformed3() {
		//Test physics.actionPerformed().
		//Test single character wall collision:
		
		fail("Not yet implemented");
	}
	@Test
	public void testUpdateCharacter() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateCollidable_Circle() {
		fail("Not yet implemented");
	}

	@Test
	public void testDead() {
		Character c1 = new Character(10, -1, 50, 25, Heading.STILL, Class.HORSE, 0, "Test");
		assertFalse("Character shouldn't be dead yet.",physics.dead(c1));
		c1.setX(-50);
		assertTrue("Character should be dead now.", physics.dead(c1));
		for(int i = 0; i < 1000; i++) {
			c1.setX((Math.random() * 200) - 100);
			if(verbose) System.out.println("Character x: " + c1.getX());
			assertTrue("Random Dead Test", physics.dead(c1) == (c1.getX() < -50));
		}
	}

	@Test
	public void testCalculateWallCollisions() {
		Character c1 = new Character(10,55,55,25,Heading.NE, Class.WARRIOR, 0, "Player0");
		//top-left corner tile
		r.getMap().getTiles()[0][0] = Tile.WALL;
		//move character into wall:
		double dx = c1.getDx();
		double dy = c1.getDy();
		//not touching
		physics.calculateWallCollisions(c1);
		// should be no changes:
		assertTrue(Double.compare(c1.getDx(), dx) == 0);
		assertTrue(Double.compare(c1.getDy(), dy) == 0);
		
		dx = -5;
		dy = -5;
		c1.setDx(dx);
		c1.setDy(dy);
		// move character:
		physics.move(c1);
		
		// should be touching
		physics.calculateWallCollisions(c1);
		// should have bounced off:
		assertTrue(Double.compare(c1.getDx(), dx) == 0);
		assertTrue(Double.compare(c1.getDy(), dy) == 0);
		
		
		//left wall
		for(int i = 0; i < r.getMap().getTiles()[0].length; i++)
			r.getMap().getTiles()[0][i] = Tile.WALL;
		
		//wall is box; 
	}

	@Test
	public void testSpecial() {
		// set stamina
		// set blocking/notblocking
		// set dashing/notdashing
		// set block/dashtimer
		Character c = new Character();
		c.setBlocking(false);
		c.setDashing(false);
		c.resetDashTimer();
		c.resetBlockTimer();
		c.setStamina(c.getMaxStamina());
		assertFalse(physics.special(c));
		
		c.setDashing(true);
		assertTrue(physics.special(c));
		
		c.setBlocking(true);
		c.setDashing(true);
		assertFalse(physics.special(c));
		
		c.setDashing(false);
		assertTrue(physics.special(c));
		
		c.setBlocking(false);
		c.setDashing(true);
		c.setStamina(0);
		assertFalse(physics.special(c));
		
		c.setBlocking(true);
		c.setDashing(false);
		c.resetDashTimer();
		c.resetBlockTimer();
		assertFalse(physics.special(c));
		
		c.incrementDashTimer();
		c.setBlocking(false);
		c.setDashing(true);
		assertTrue(physics.special(c));
		
		c.resetDashTimer();
		c.incrementBlockTimer();
		c.setBlocking(true);
		c.setDashing(false);
		assertTrue(physics.special(c));
	}

	@Test
	public void testMove() {
		// calculate dx,dy; apply them to x,y.
		Character c1 = new Character(1, 50, 50, 25, Heading.STILL, Class.WARRIOR, 0, "Player 1");
		double x = 50,y = 50;
		for(double dx = -50; dx < 51; dx++){
			for(double dy = -50; dy < 51; dy++){
				c1.setX(x);
				c1.setY(y);
				c1.setDx(dx);
				c1.setDy(dy);
				physics.move(c1);
				assertTrue("Moving in the x-axis.", Double.compare(c1.getX(), x + dx) == 0);
				assertTrue("Moving in the y-axis.", Double.compare(c1.getY(), y + dy) == 0);
			}
		}
	}

	@Test
	public void testCollideCollidableCollidable() {
		fail("Not yet implemented");
	}

	@Test
	public void testCollideCharacterCharacter() {
		fail("Not yet implemented");
	}

	@Test
	public void testDetectCollision() {
		Character c1 = new Character(10, 26, 50, 25, Heading.E, Class.HORSE, 0, "C1");
		Character c2 = new Character(10, 75, 50, 25, Heading.W, Class.WARRIOR, 1, "C2");
		Physics.CND cnd = physics.detectCollision(c1,c2);
		assertTrue("Collision Detection 1",cnd.collided);
		c1.setX(20);
		cnd = physics.detectCollision(c1,c2);
		assertTrue("Collision Detection 2 (No collision)",!cnd.collided);
		for(int i = 0; i < 1000; i++){
			//1000 tests
			c1.setX(Math.random() * 100);
			c1.setY(Math.random() * 100);
			c2.setX(Math.random() * 100);
			c2.setY(Math.random() * 100);
			cnd = physics.detectCollision(c1, c2);
			boolean collision = false;
			double dist = Math.sqrt(Math.pow(Math.abs(c1.getX()) - Math.abs(c2.getX()), 2) + Math.pow(Math.abs(c1.getY()) - Math.abs(c2.getY()), 2));
			if(dist <= (c1.getRadius() + c2.getRadius())) {
				collision = true;
			}
			if(verbose) System.out.println("Distance between two characters: " + dist + ", radius: 25.0, collisionDetected: " + cnd.collided);
			assertTrue("Collision Detection Random Test", cnd.collided == collision);
		}
	}

	@Test
	public void testDash() {
		Character c = new Character();
		c.setStamina(60);
		// test while not moving
		c.setDx(0);
		c.setDy(0);
		physics.dash(c);
		assertTrue(c.getStamina() == 150);
		assertTrue(c.getDx() == 0);
		assertTrue(c.getDy() == 0);
		// test while moving in 1 direction, 25 times
		c.setDx(1);
		c.setDy(0);
		c.setLeft(true);
		physics.dash(c);
		assertTrue(c.getStamina() == 150);
		assertTrue(c.getMovingDirection() == Heading.W);
		assertTrue(c.getDx() == 2*-c.getMaxDx());
		for (int i = 0; i < 24; i++) {
			physics.dash(c);
		}
		assertFalse(c.isDashing());
		assertTrue(c.getDashTimer() == 0);
		assertTrue(c.getDx() == -c.getMaxDx());
	}

	@Test
	public void testBlock() {
		Character c = new Character();
		c.setStamina(75);
		double initialSpeed = 1;
		double initialMass = c.getMass();
		c.setDx(initialSpeed);
		c.setDy(initialSpeed);
		physics.block(c);
		assertTrue(c.getStamina() == 75);
		assertTrue(c.getMass() == 10 * initialMass);
		for (int i = 0; i < 24; i++) {
			physics.block(c);
		}
		assertFalse(c.isBlocking());
		assertTrue(c.getBlockTimer() == 0);
		assertTrue(c.getDx() < initialSpeed);
	}
}
