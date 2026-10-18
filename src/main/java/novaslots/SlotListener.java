package com.novaslots;


import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerPing;



public class SlotListener {


    private final ProxyServer server;



    public SlotListener(ProxyServer server){

        this.server = server;

    }



    @Subscribe
    public void onPing(ProxyPingEvent event){


        int online =
                server.getPlayerCount();


        int max =
                online + Novaslots.extraSlots;



        ServerPing ping =
                event.getPing();



        event.setPing(

                ping.asBuilder()

                        .onlinePlayers(
                                online
                        )

                        .maximumPlayers(
                                max
                        )

                        .build()

        );


    }


}