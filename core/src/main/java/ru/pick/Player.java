package ru.pick;


public class Player {
    public String firebaseId;
    public String name = "Noname";
    public int level;
    public int money;
    public int rank;

    public void copy(Player p){
        name=p.name;
        level =p. level;
        money=p.money;
    }
    public void clear() {
        money=0;
        level=0;



    }


    public Player() {
        }



    public void savePlayerToFirebase(Player player){
        FirebaseManager firebase = FirebaseService.create();
        firebase.savePlayer(player);
    }

   public void setMoney(int money) {
            this.money = money;
        }

   public String getName() {
            return name;
        }

   public void setName(String name) {
            this.name = name;
        }

   public int getLevel() {
            return level;
        }

   public void setLevel(int level) {
            this.level= level;
        }
  }







