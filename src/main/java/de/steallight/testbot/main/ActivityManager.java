package de.steallight.testbot.main;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.ShardManager;

import java.util.Timer;
import java.util.TimerTask;

public class ActivityManager {
    private Runnable switcherThread;
    private final Bot bot;

    public ActivityManager(Bot bot) {
        this.bot = bot;

    }

    public void loadPresence() {
        ThreadHandler.startExecute(this.switcherThread = () -> {
            final Timer timer = new Timer();

            timer.scheduleAtFixedRate(new TimerTask() {
                int currentRoutinePosition = 0;

                @Override
                public void run() {
                    switch (this.currentRoutinePosition) {
                        case 0:
                            Bot.jda.getPresence().setPresence(OnlineStatus.DO_NOT_DISTURB, Activity.playing("Rocket League"));
                            this.currentRoutinePosition++;
                            break;
                        case 1:
                            Bot.jda.getPresence().setPresence(OnlineStatus.DO_NOT_DISTURB, Activity.watching("Chats an"));
                            this.currentRoutinePosition++;
                            break;
                        case 2:
                            Bot.jda.getPresence().setPresence(OnlineStatus.DO_NOT_DISTURB, Activity.listening("auf -> !"));
                            this.currentRoutinePosition = 0;
                            break;


                    }
                }
            }, 1, 60000);//<-- In Millisekunden
        });
    }

    public void stopPresence() {
        ThreadHandler.removeExecute(this.switcherThread);
    }
}
