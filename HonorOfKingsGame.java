import java.util.*;
import java.util.concurrent.TimeUnit;

public class HonorOfKingsGame {
    private static final Random random = new Random();
    private static final Scanner scanner = new Scanner(System.in);

    // Hero class (unchanged)
    static class Hero {
        String name;
        String role;
        int hp;
        int attack;
        int defense;
        int abilityPower;
        List<String> abilities = new ArrayList<>();
        int level;
        int gold;
        boolean isAlive = true;

        public Hero(String name, String role, int hp, int attack, int defense, int abilityPower) {
            this.name = name;
            this.role = role;
            this.hp = hp;
            this.attack = attack;
            this.defense = defense;
            this.abilityPower = abilityPower;
            this.level = 1;
            this.gold = 300;
            initializeAbilities();
        }

        private void initializeAbilities() {
            abilities.add("Basic Attack");
            abilities.add("Ability 1");
            abilities.add("Ability 2");
            abilities.add("Ultimate");
        }

        public void levelUp() {
            level++;
            hp += 100;
            attack += 10;
            defense += 5;
            abilityPower += 8;
            System.out.println(name + " leveled up to " + level + "!");
        }

        public void earnGold(int amount) {
            gold += amount;
            System.out.println(name + " earned " + amount + " gold. Total: " + gold);
        }

        public void buyItem(String item) {
            int cost = 200;
            if (gold >= cost) {
                gold -= cost;
                System.out.println(name + " purchased " + item + "!");
                if (item.contains("Sword")) attack += 20;
                else if (item.contains("Armor")) defense += 15;
                else if (item.contains("Tome")) abilityPower += 25;
            } else {
                System.out.println("Not enough gold for " + item);
            }
        }

        public void attack(Hero target) {
            int damage = Math.max(5, attack - target.defense / 2);
            target.takeDamage(damage, this);
            System.out.println(name + " basic attacks " + target.name + " for " + damage + " damage!");
        }

        public void useAbility(int abilityIndex, Hero target) {
            if (abilityIndex < 1 || abilityIndex > abilities.size()) return;

            String ability = abilities.get(abilityIndex - 1);
            int damage = 0;

            switch (ability) {
                case "Ability 1":
                    damage = abilityPower / 2 + random.nextInt(20);
                    break;
                case "Ability 2":
                    damage = abilityPower / 3 + random.nextInt(15);
                    break;
                case "Ultimate":
                    damage = abilityPower + random.nextInt(40);
                    break;
            }

            if (ability.equals("Basic Attack")) {
                attack(target);
            } else {
                System.out.println(name + " uses " + ability + " on " + target.name + " for " + damage + " damage!");
                target.takeDamage(damage, this);
            }
        }

        public void takeDamage(int damage, Hero source) {
            hp -= damage;
            if (hp <= 0) {
                hp = 0;
                isAlive = false;
                System.out.println(name + " has been slain by " + source.name + "!");
                source.earnGold(200);
            }
        }

        public void respawn() {
            if (!isAlive) {
                hp = 500 + (level * 50);
                isAlive = true;
                System.out.println(name + " has respawned!");
            }
        }

        @Override
        public String toString() {
            return name + " (" + role + ") Lvl " + level + " | HP: " + hp + " | ATK: " + attack + 
                   " | DEF: " + defense + " | AP: " + abilityPower + " | Gold: " + gold;
        }
    }

    // Team class (unchanged)
    static class Team {
        String name;
        List<Hero> heroes = new ArrayList<>();
        int kills = 0;
        int towers = 5;

        public Team(String name) {
            this.name = name;
        }

        public boolean isDefeated() {
            return towers <= 0;
        }

        public boolean allDead() {
            for (Hero hero : heroes) {
                if (hero.isAlive) return false;
            }
            return true;
        }

        public void respawnAll() {
            for (Hero hero : heroes) {
                hero.respawn();
            }
        }

        public void addHero(Hero hero) {
            if (heroes.size() < 5) {
                heroes.add(hero);
            }
        }

        public void printAliveHeroes() {
            System.out.println("\n" + name + " Heroes:");
            for (int i = 0; i < heroes.size(); i++) {
                Hero hero = heroes.get(i);
                if (hero.isAlive) {
                    System.out.println(i + ". " + hero);
                }
            }
        }
    }

    // Game map (unchanged)
    static class GameMap {
        Team blueTeam;
        Team redTeam;
        int gameTime = 0;

        public GameMap(Team blueTeam, Team redTeam) {
            this.blueTeam = blueTeam;
            this.redTeam = redTeam;
        }

        public void simulateMinionWave() {
            System.out.println("\nMinion waves clash in the lanes!");
            if (random.nextBoolean()) {
                System.out.println("Blue team minions gain slight advantage");
            } else {
                System.out.println("Red team minions gain slight advantage");
            }
        }

        public void jungleCreepSpawn() {
            if (gameTime % 90 == 0) {
                System.out.println("\nJungle creeps have spawned!");
            }
        }

        public void updateGameTime() {
            gameTime += 30;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== HONOR OF KINGS 5v5 GAME ===");
        System.out.println("You control the BLUE TEAM. The RED TEAM is AI-controlled.\n");

        // Create heroes (unchanged)
        Hero luban = new Hero("Lu Ban", "Marksman", 600, 60, 20, 10);
        Hero diaochan = new Hero("Diao Chan", "Mage", 500, 40, 15, 80);
        Hero zhaoyun = new Hero("Zhao Yun", "Warrior", 800, 70, 40, 20);
        Hero houyi = new Hero("Hou Yi", "Marksman", 550, 65, 18, 15);
        Hero zhangfei = new Hero("Zhang Fei", "Tank", 1000, 50, 60, 5);

        Hero arthur = new Hero("Arthur", "Warrior", 850, 65, 45, 15);
        Hero eudora = new Hero("Eudora", "Mage", 480, 35, 12, 85);
        Hero miya = new Hero("Miya", "Marksman", 580, 70, 15, 10);
        Hero alice = new Hero("Alice", "Support", 650, 30, 25, 50);
        Hero tigreal = new Hero("Tigreal", "Tank", 950, 55, 65, 5);

        // Create teams
        Team blueTeam = new Team("Blue Team");
        blueTeam.addHero(luban);
        blueTeam.addHero(diaochan);
        blueTeam.addHero(zhaoyun);
        blueTeam.addHero(houyi);
        blueTeam.addHero(zhangfei);

        Team redTeam = new Team("Red Team");
        redTeam.addHero(arthur);
        redTeam.addHero(eudora);
        redTeam.addHero(miya);
        redTeam.addHero(alice);
        redTeam.addHero(tigreal);

        GameMap map = new GameMap(blueTeam, redTeam);

        // Main game loop
        while (!blueTeam.isDefeated() && !redTeam.isDefeated()) {
            System.out.println("\n=== GAME TIME: " + map.gameTime/60 + "m " + map.gameTime%60 + "s ===");
            
            // Minions and jungle
            map.simulateMinionWave();
            map.jungleCreepSpawn();
            
            // PLAYER TURN (Blue Team)
            System.out.println("\n=== YOUR TURN (Blue Team) ===");
            for (Hero hero : blueTeam.heroes) {
                if (hero.isAlive) {
                    System.out.println("\nCurrent Hero: " + hero);
                    hero.earnGold(50 + random.nextInt(30));
                    if (random.nextBoolean()) hero.levelUp();
                    
                    // Show available enemies
                    System.out.println("\nAvailable Enemies:");
                    List<Hero> aliveEnemies = new ArrayList<>();
                    for (int i = 0; i < redTeam.heroes.size(); i++) {
                        Hero enemy = redTeam.heroes.get(i);
                        if (enemy.isAlive) {
                            System.out.println(i + ". " + enemy);
                            aliveEnemies.add(enemy);
                        }
                    }
                    
                    if (!aliveEnemies.isEmpty()) {
                        // Player choice: action
                        System.out.println("\nChoose action:");
                        for (int i = 0; i < hero.abilities.size(); i++) {
                            System.out.println((i+1) + ". " + hero.abilities.get(i));
                        }
                        System.out.println("5. Buy Item");
                        System.out.print("Your choice (1-5): ");
                        int action = scanner.nextInt();
                        
                        if (action == 5) {
                            // Buy item
                            System.out.println("\nAvailable Items:");
                            System.out.println("1. Iron Sword (+20 ATK) - 200 gold");
                            System.out.println("2. Steel Armor (+15 DEF) - 200 gold");
                            System.out.println("3. Magic Tome (+25 AP) - 200 gold");
                            System.out.print("Choose item (1-3): ");
                            int itemChoice = scanner.nextInt();
                            String item = "";
                            switch(itemChoice) {
                                case 1: item = "Iron Sword"; break;
                                case 2: item = "Steel Armor"; break;
                                case 3: item = "Magic Tome"; break;
                            }
                            hero.buyItem(item);
                        } else {
                            // Choose target
                            System.out.print("Choose target (0-" + (aliveEnemies.size()-1) + "): ");
                            int targetIndex = scanner.nextInt();
                            Hero target = aliveEnemies.get(targetIndex);
                            hero.useAbility(action, target);
                        }
                    }
                }
            }
            
            // AI TURN (Red Team)
            System.out.println("\n=== AI TURN (Red Team) ===");
            for (Hero hero : redTeam.heroes) {
                if (hero.isAlive) {
                    hero.earnGold(50 + random.nextInt(30));
                    if (random.nextBoolean()) hero.levelUp();
                    
                    List<Hero> aliveEnemies = new ArrayList<>();
                    for (Hero enemy : blueTeam.heroes) {
                        if (enemy.isAlive) aliveEnemies.add(enemy);
                    }
                    
                    if (!aliveEnemies.isEmpty()) {
                        Hero target = aliveEnemies.get(random.nextInt(aliveEnemies.size()));
                        int action = random.nextInt(4) + 1;
                        hero.useAbility(action, target);
                        
                        if (random.nextInt(10) < 3) {
                            String[] items = {"Iron Sword", "Magic Tome", "Steel Armor"};
                            hero.buyItem(items[random.nextInt(items.length)]);
                        }
                    }
                }
            }
            
            // Check for team wipes
            if (blueTeam.allDead()) {
                System.out.println("\nYOUR TEAM WIPED OUT!");
                redTeam.kills += 5;
                if (random.nextBoolean()) {
                    redTeam.towers--;
                    System.out.println("RED TEAM DESTROYED A BLUE TOWER!");
                }
                blueTeam.respawnAll();
            }
            
            if (redTeam.allDead()) {
                System.out.println("\nRED TEAM WIPED OUT!");
                blueTeam.kills += 5;
                if (random.nextBoolean()) {
                    blueTeam.towers--;
                    System.out.println("YOUR TEAM DESTROYED A RED TOWER!");
                }
                redTeam.respawnAll();
            }
            
            // Update game state
            map.updateGameTime();
            TimeUnit.SECONDS.sleep(1);
            
            // Display game status
            System.out.println("\n=== SCOREBOARD ===");
            System.out.println("Your Team | Kills: " + blueTeam.kills + " | Towers: " + blueTeam.towers);
            System.out.println("AI Team   | Kills: " + redTeam.kills + " | Towers: " + redTeam.towers);
        }
        
        // Game over
        System.out.println("\n=== GAME OVER ===");
        if (blueTeam.isDefeated()) {
            System.out.println("AI TEAM VICTORY!");
        } else {
            System.out.println("YOUR TEAM VICTORY!");
        }
    }
}