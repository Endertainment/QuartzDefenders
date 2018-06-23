package ua.coolboy.quartzdefenders.turrets;

import org.bukkit.event.Listener;

public class TurretListener implements Listener {

    /*QuartzDefenders plugin;

    public TurretListener(QuartzDefenders plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void interactEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        boolean nearStand = false;
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)
                //&& event.getClickedBlock().getType().equals(Material.STRUCTURE_BLOCK)
                && event.hasItem()
                && event.getItem().getType().equals(Material.IRON_BARDING)
                && event.getItem().hasItemMeta()
                && event.getItem().getItemMeta().hasLore()
                && event.getItem().getItemMeta().getLore().contains("Creates a turret")) {
            if (!player.getGameMode().equals(GameMode.CREATIVE)) {
                if (player.getInventory().getItemInMainHand().getAmount() > 1) {
                    player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
                } else {
                    player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                }
            }
            Block block = event.getClickedBlock();
            Location location = block.getLocation();
            Collection<Entity> near = block.getLocation().add(0, 1, 0).getWorld().getNearbyEntities(block.getLocation(), 2, 2, 2);
            for (Entity st : near) {
                if (st instanceof ArmorStand && st.getName().startsWith("Turret")) {
                    nearStand = true;
                }
            }
            GameTeam team;
            try {
                team = QuartzDefenders.getInstance().getGame(player).getTeam(player);
            } catch (NullPointerException ex) {
                QuartzDefenders.sendInfo("Player " + player.getName() + " used turret without a team!");
                return;
            }
            if (!nearStand) {
                Turret turret = new Turret(player, team, location, plugin);
                runTurret(turret, player);
            }
        }
    }

    public void runTurret(Turret stand, Player player) {
        ArmorStand turret = stand.getStand();
        //int liveTime = plugin.getGame(player).getTurretLivetime();
        int liveTime = 300 * 20;
        new BukkitRunnable() {
            @Override
            public void run() {
                boolean shooted = false;
                if (turret.isDead()) {
                    turret.remove();
                    this.cancel();
                }
                for (Entity e : turret.getNearbyEntities(1, 1, 1)) {
                    e.teleport(e.getLocation().add(Mobs.randomInRadius(1), 0, Mobs.randomInRadius(1)));
                }
                List<Entity> list = turret.getNearbyEntities(7, 5, 7);
                if (Mobs.countMobs(list, EntityType.PLAYER) > 0) {
                    for (Entity mob : list) {
                        if (mob instanceof Player) {
                            Player playery = (Player) mob;
                            if (!mob.getName().equals(player.getName())
                                    && !playery.getGameMode().equals(GameMode.SPECTATOR)
                                    && !playery.getGameMode().equals(GameMode.CREATIVE)
                                    //&& !plugin.getGame(playery).getTeam(turret.getMetadata("team").get(0).asString()).contains(plugin.getGamePlayer(playery))) {
                                    && !plugin.getGame(playery).getTeam(playery).equals(stand.getTeam())) {
                                Vector vector = mob.getLocation().toVector().subtract(turret.getLocation().toVector());

                                stand.shoot(vector);

                                stand.lookAtTarget(mob);
                                shooted = true;
                                break;
                            }
                        }
                    }
                }
                if (!shooted && (Mobs.countMobs(list, EntityType.SKELETON) > 0 || Mobs.countMobs(list, EntityType.WITHER_SKELETON) > 0)) {
                    for (Entity mob : list) {
                        if (mob instanceof Skeleton || mob instanceof WitherSkeleton) {
                            Vector vector = mob.getLocation().toVector().subtract(turret.getLocation().toVector());
                            stand.shoot(vector);
                            stand.lookAtTarget(mob);
                            break;
                        }
                    }
                }

                turret.setMetadata("lived", new FixedMetadataValue(plugin, turret.getMetadata("lived").get(0).asInt() + 40));
                turret.setCustomName("Turret (" + Integer.toString((liveTime - turret.getMetadata("lived").get(0).asInt()) / 20) + "sec)");
                if (turret.getMetadata("lived").get(0).asInt() > liveTime) {
                    turret.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, turret.getLocation(), 300);
                    turret.getLocation().getWorld().playSound(turret.getLocation(), Sound.ENTITY_BLAZE_HURT, 50, 0);
                    turret.getLocation().getWorld().createExplosion(turret.getLocation(), 2.0F);
                    turret.remove();
                    stand.remove();
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 40);
    }*/
}
