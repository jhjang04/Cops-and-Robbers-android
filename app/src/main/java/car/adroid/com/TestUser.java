package car.adroid.com;

/**
 * Created by mbj94 on 2016-12-08.
 */

public class TestUser{

    public int team;
    public int state;
    public String nickname;

    public int getTeam() {
        return team;
    }

    public int getState(){
        return state;
    }
    public String getNickName(){return nickname;}
    public int getReadyStatus(){return state;}


    TestUser(int team, int state, String nickname){
        this.team = team;
        this.state = state;
        this.nickname = nickname;
    }
};