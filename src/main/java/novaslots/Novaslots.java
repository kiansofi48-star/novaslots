package com.novaslots;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import org.slf4j.Logger;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.nio.file.Path;


@Plugin(
        id = "novaslots",
        name = "NovaSlots",
        version = "1.0"
)

public class Novaslots {

    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;

    private YamlConfigurationLoader loader;
    private CommentedConfigurationNode config;


    public static int extraSlots = 1;


    @Inject
    public Novaslots(
            ProxyServer server,
            Logger logger,
            @DataDirectory Path dataDirectory
    ){

        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;

    }



    @Subscribe
    public void onInit(ProxyInitializeEvent event){

        loadConfig();

        registerCommand();


        server.getEventManager()
                .register(this, new SlotListener(server));


        logger.info("NovaSlots enabled!");

    }




    private void loadConfig(){

        try {

            File dir = dataDirectory.toFile();

            if(!dir.exists())
                dir.mkdirs();


            File file = new File(dir, "config.yml");


            loader = YamlConfigurationLoader.builder()
                    .file(file)
                    .build();



            if(!file.exists()){

                config = loader.createNode();

                config.node("extra-slots")
                        .set(1);

                loader.save(config);

            }



            config = loader.load();


            extraSlots =
                    config.node("extra-slots")
                            .getInt(1);



        }
        catch(Exception e){

            logger.error("Failed to load config", e);

        }

    }





    private void registerCommand(){

        CommandManager manager =
                server.getCommandManager();



        SimpleCommand command = invocation -> {


            String[] args =
                    invocation.arguments();



            if(args.length > 0 &&
                    args[0].equalsIgnoreCase("reload")){


                loadConfig();


                invocation.source()
                        .sendMessage(
                                Component.text(
                                                "NovaSlots reloaded!"
                                        )
                                        .color(
                                                NamedTextColor.GREEN
                                        )
                        );

            }


        };



        manager.register(
                manager.metaBuilder("ns")
                        .aliases("novaslots")
                        .build(),

                command
        );

    }

}