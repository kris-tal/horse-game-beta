package services.listeners;

import data.race.PlayerProgressResponse;

import java.util.List;

public interface GameListener {
    void onProgressUpdate(List<PlayerProgressResponse.PlayerProgress> newProgress);

    void onGameWon(String winnerUsername); //Will be changed to contain some more information maybe

    void onGameClosed();//? , lobby close might happen during the game
}