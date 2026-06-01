package de.btegermany.terraplusminus.commands;

import de.btegermany.terraplusminus.gen.RealWorldGenerator;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.buildtheearth.terraminusminus.projection.GeographicProjection;
import net.buildtheearth.terraminusminus.projection.OutOfProjectionBoundsException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

public class DistortionCommand implements BasicCommand {

    @Override
    public void execute(@NotNull CommandSourceStack stack, @NotNull String[] args) {

        if (!(stack.getSender() instanceof Player player)) {
            stack.getSender().sendMessage(
                    Component.text("Players only.", NamedTextColor.RED)
            );
            return;
        }

        if (!player.hasPermission("t+-.distortion")) {
            player.sendMessage(
                    Component.text("No permission for /distortion", NamedTextColor.RED)
            );
            return;
        }

        World world = player.getWorld();
        ChunkGenerator generator = world.getGenerator();

        if (!(generator instanceof RealWorldGenerator realGen)) {
            player.sendMessage(
                    Component.text("This world is not using Terra+- generator.", NamedTextColor.RED)
            );
            return;
        }

        GeographicProjection projection = realGen.getSettings().projection();

        try {
            double[] geo = projection.toGeo(
                    player.getLocation().getX(),
                    player.getLocation().getZ()
            );

            double[] tissot = projection.tissot(
                    geo[0],
                    geo[1]
            );

            double scale = Math.sqrt(Math.abs(tissot[0]));
            double angle = Math.toDegrees(tissot[1]);

            player.sendMessage(
                    Component.text()
                            .append(Component.text("Distortion: ", NamedTextColor.GRAY))
                            .appendNewline()
                            .append(Component.text(
                                    String.format("~%.6f blocks/meter", scale),
                                    NamedTextColor.WHITE
                            ))
                            .append(Component.newline())
                            .append(Component.text(
                                    String.format("± %.4f°", angle),
                                    NamedTextColor.WHITE
                            ))
                            .build()
            );

        } catch (OutOfProjectionBoundsException e) {
            player.sendMessage(
                    Component.text(
                            "You are outside the projection bounds.",
                            NamedTextColor.RED
                    )
            );
        }
    }
}
