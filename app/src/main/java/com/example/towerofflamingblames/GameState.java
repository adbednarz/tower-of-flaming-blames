package com.example.towerofflamingblames;

import com.example.towerofflamingblames.GameObjects.IGameArtefact;
import com.example.towerofflamingblames.GameObjects.IGameObject;

import java.util.ArrayList;
import java.util.LinkedList;

public class GameState {

    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    ///// PLAYERS
    public final static int PLAYER_HEIGHT_PERCENTAGE = 10;
    public static float PLAYER_JUMP_VELOCITY = 20.0f;
    public final static float PLAYER_GRAVITY_VELOCITY = 0.3f;

    ////
    public static LinkedList<IGameObject> platforms = new LinkedList<>();
    // wielkość pojedynczej kostki platformy
    public static int PLATFORM_SIZE = 100;
    // wskazuje z ilu kostek maksymalnie może składać się platforma
    public static int MAX_PLATFORM_LENGTH = 5;
    // płynne przesuwanie na boki oraz w dół platformy
    public static int MOVABLE_X = 3;
    public static int MOVABLE_Y = 2;
    // co ile plaform ma się pojawiać ruchoma platforma
    public static int MOVABLE_PLATFORMS_FREQUENCY = 5;
    // odległość pomiędzy sąsiednimi platformami w szerokości
    public static int PLATFORM_GAP_X = 100;
    // odległość pomiędzy sąsiednimi platformami w wysokości
    public static int PLATFORM_GAP_Y;

    // rozmiar różnych artefaktów w grze
    public static int ARTEFACT_SIZE = 80;
    public static ArrayList<IGameArtefact> artefacts = new ArrayList<>();
    // częstotliwość występowania monet
    public static int COINS_FREQUENCY = 5;

    // spowolnienie poruszania się "okna",gdy gracz jest powyżej połowy ekranu
    public static float MOVE_SCENE_SLOW_RATIO = 0.03f;
}
