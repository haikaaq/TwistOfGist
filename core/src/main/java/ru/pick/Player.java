package ru.pick;


public class Player {
    String name = "Noname";
    int level=1;
    int money;

    public void copy(Player p){
        name=p.name;
        level =p. level;
        money=p.money;
    }
    public void clear() {
        money=0;
        level=1;



    }

    // Пустой конструктор (обязательно для Firebase)
    public Player() {
        }

        // Конструктор с параметрами (опционально)


        // Геттеры и сеттеры (обязательно для Firebase)



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







