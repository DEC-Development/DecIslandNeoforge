package net.minecraft.client.resources.model;

import com.mojang.logging.LogUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.SpriteLoader;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.metadata.gui.GuiMetadataSection;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.metadata.MetadataSectionType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.slf4j.Logger;

@OnlyIn(Dist.CLIENT)
public class AtlasManager implements PreparableReloadListener, MaterialSet, AutoCloseable {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final List<AtlasManager.AtlasConfig> KNOWN_ATLASES = List.of(
        new AtlasManager.AtlasConfig(Sheets.ARMOR_TRIMS_SHEET, AtlasIds.ARMOR_TRIMS, false),
        new AtlasManager.AtlasConfig(Sheets.BANNER_SHEET, AtlasIds.BANNER_PATTERNS, false),
        new AtlasManager.AtlasConfig(Sheets.BED_SHEET, AtlasIds.BEDS, false),
        new AtlasManager.AtlasConfig(TextureAtlas.LOCATION_BLOCKS, AtlasIds.BLOCKS, true),
        new AtlasManager.AtlasConfig(TextureAtlas.LOCATION_ITEMS, AtlasIds.ITEMS, false),
        new AtlasManager.AtlasConfig(Sheets.CHEST_SHEET, AtlasIds.CHESTS, false),
        new AtlasManager.AtlasConfig(Sheets.DECORATED_POT_SHEET, AtlasIds.DECORATED_POT, false),
        new AtlasManager.AtlasConfig(Sheets.GUI_SHEET, AtlasIds.GUI, false, Set.of(GuiMetadataSection.TYPE)),
        new AtlasManager.AtlasConfig(Sheets.MAP_DECORATIONS_SHEET, AtlasIds.MAP_DECORATIONS, false),
        new AtlasManager.AtlasConfig(Sheets.PAINTINGS_SHEET, AtlasIds.PAINTINGS, false),
        new AtlasManager.AtlasConfig(TextureAtlas.LOCATION_PARTICLES, AtlasIds.PARTICLES, false),
        new AtlasManager.AtlasConfig(Sheets.SHIELD_SHEET, AtlasIds.SHIELD_PATTERNS, false),
        new AtlasManager.AtlasConfig(Sheets.SHULKER_SHEET, AtlasIds.SHULKER_BOXES, false),
        new AtlasManager.AtlasConfig(Sheets.SIGN_SHEET, AtlasIds.SIGNS, false),
        new AtlasManager.AtlasConfig(Sheets.CELESTIAL_SHEET, AtlasIds.CELESTIALS, false)
    );
    public static final PreparableReloadListener.StateKey<AtlasManager.PendingStitchResults> PENDING_STITCH = new PreparableReloadListener.StateKey<>();
    private final Map<Identifier, AtlasManager.AtlasEntry> atlasByTexture = new HashMap<>();
    private final Map<Identifier, AtlasManager.AtlasEntry> atlasById = new HashMap<>();
    private Map<Material, TextureAtlasSprite> materialLookup = Map.of();
    private int maxMipmapLevels;

    public AtlasManager(TextureManager textureManager, int maxMipmapLevels) {
        List<AtlasManager.AtlasConfig> KNOWN_ATLASES = net.neoforged.neoforge.client.ClientHooks.gatherTextureAtlases(AtlasManager.KNOWN_ATLASES);
        for (AtlasManager.AtlasConfig atlasmanager$atlasconfig : KNOWN_ATLASES) {
            TextureAtlas textureatlas = new TextureAtlas(atlasmanager$atlasconfig.textureId);
            textureManager.register(atlasmanager$atlasconfig.textureId, textureatlas);
            AtlasManager.AtlasEntry atlasmanager$atlasentry = new AtlasManager.AtlasEntry(textureatlas, atlasmanager$atlasconfig);
            this.atlasByTexture.put(atlasmanager$atlasconfig.textureId, atlasmanager$atlasentry);
            this.atlasById.put(atlasmanager$atlasconfig.definitionLocation, atlasmanager$atlasentry);
        }

        this.maxMipmapLevels = maxMipmapLevels;
    }

    public TextureAtlas getAtlasOrThrow(Identifier id) {
        AtlasManager.AtlasEntry atlasmanager$atlasentry = this.atlasById.get(id);
        if (atlasmanager$atlasentry == null) {
            throw new IllegalArgumentException("Invalid atlas id: " + id);
        } else {
            return atlasmanager$atlasentry.atlas();
        }
    }

    public void forEach(BiConsumer<Identifier, TextureAtlas> action) {
        this.atlasById.forEach((p_467809_, p_436509_) -> action.accept(p_467809_, p_436509_.atlas));
    }

    public void updateMaxMipLevel(int maxMipLevel) {
        this.maxMipmapLevels = maxMipLevel;
    }

    @Override
    public void close() {
        this.materialLookup = Map.of();
        this.atlasById.values().forEach(AtlasManager.AtlasEntry::close);
        this.atlasById.clear();
        this.atlasByTexture.clear();
    }

    @Override
    public TextureAtlasSprite get(Material p_433878_) {
        TextureAtlasSprite textureatlassprite = this.materialLookup.get(p_433878_);
        if (textureatlassprite != null) {
            return textureatlassprite;
        } else {
            Identifier identifier = p_433878_.atlasLocation();
            AtlasManager.AtlasEntry atlasmanager$atlasentry = this.atlasByTexture.get(identifier);
            if (atlasmanager$atlasentry == null) {
                throw new IllegalArgumentException("Invalid atlas texture id: " + identifier);
            } else {
                return atlasmanager$atlasentry.atlas().missingSprite();
            }
        }
    }

    @Override
    public void prepareSharedState(PreparableReloadListener.SharedState p_434399_) {
        int i = this.atlasById.size();
        List<AtlasManager.PendingStitch> list = new ArrayList<>(i);
        Map<Identifier, CompletableFuture<SpriteLoader.Preparations>> map = new HashMap<>(i);
        List<CompletableFuture<?>> list1 = new ArrayList<>(i);
        this.atlasById.forEach((p_469963_, p_434021_) -> {
            CompletableFuture<SpriteLoader.Preparations> completablefuture1 = new CompletableFuture<>();
            map.put(p_469963_, completablefuture1);
            list.add(new AtlasManager.PendingStitch(p_434021_, completablefuture1));
            list1.add(completablefuture1.thenCompose(SpriteLoader.Preparations::readyForUpload));
        });
        CompletableFuture<?> completablefuture = CompletableFuture.allOf(list1.toArray(CompletableFuture[]::new));
        p_434399_.set(PENDING_STITCH, new AtlasManager.PendingStitchResults(list, map, completablefuture));
    }

    @Override
    public CompletableFuture<Void> reload(
        PreparableReloadListener.SharedState p_432891_, Executor p_435942_, PreparableReloadListener.PreparationBarrier p_434562_, Executor p_433243_
    ) {
        AtlasManager.PendingStitchResults atlasmanager$pendingstitchresults = p_432891_.get(PENDING_STITCH);
        ResourceManager resourcemanager = p_432891_.resourceManager();
        atlasmanager$pendingstitchresults.pendingStitches
            .forEach(p_435220_ -> p_435220_.entry.scheduleLoad(resourcemanager, p_435942_, this.maxMipmapLevels).whenComplete((p_434695_, p_433705_) -> {
                if (p_434695_ != null) {
                    p_435220_.preparations.complete(p_434695_);
                } else {
                    p_435220_.preparations.completeExceptionally(p_433705_);
                }
            }));
        return atlasmanager$pendingstitchresults.allReadyToUpload
            .thenCompose(p_434562_::wait)
            .thenAcceptAsync(p_470514_ -> this.updateSpriteMaps(atlasmanager$pendingstitchresults), p_433243_);
    }

    private void updateSpriteMaps(AtlasManager.PendingStitchResults pendingStitchResults) {
        this.materialLookup = pendingStitchResults.joinAndUpload();
        Map<Identifier, TextureAtlasSprite> map = new HashMap<>();
        this.materialLookup
            .forEach(
                (p_470516_, p_470517_) -> {
                    if (!p_470516_.texture().equals(MissingTextureAtlasSprite.getLocation())) {
                        TextureAtlasSprite textureatlassprite = map.putIfAbsent(p_470516_.texture(), p_470517_);
                        if (textureatlassprite != null) {
                            LOGGER.warn(
                                "Duplicate sprite {} from atlas {}, already defined in atlas {}. This will be rejected in a future version",
                                p_470516_.texture(),
                                p_470516_.atlasLocation(),
                                textureatlassprite.atlasLocation()
                            );
                        }
                    }
                }
            );
    }

    @OnlyIn(Dist.CLIENT)
    public record AtlasConfig(Identifier textureId, Identifier definitionLocation, boolean createMipmaps, Set<MetadataSectionType<?>> additionalMetadata) {
        public AtlasConfig(Identifier p_469620_, Identifier p_468900_, boolean p_433479_) {
            this(p_469620_, p_468900_, p_433479_, Set.of());
        }
    }

    @OnlyIn(Dist.CLIENT)
    record AtlasEntry(TextureAtlas atlas, AtlasManager.AtlasConfig config) implements AutoCloseable {
        @Override
        public void close() {
            this.atlas.clearTextureData();
        }

        CompletableFuture<SpriteLoader.Preparations> scheduleLoad(ResourceManager resourceManager, Executor executor, int mipLevel) {
            return SpriteLoader.create(this.atlas)
                .loadAndStitch(resourceManager, this.config.definitionLocation, this.config.createMipmaps ? mipLevel : 0, executor, this.config.additionalMetadata);
        }
    }

    @OnlyIn(Dist.CLIENT)
    record PendingStitch(AtlasManager.AtlasEntry entry, CompletableFuture<SpriteLoader.Preparations> preparations) {
        public void joinAndUpload(Map<Material, TextureAtlasSprite> output) {
            SpriteLoader.Preparations spriteloader$preparations = this.preparations.join();
            this.entry.atlas.upload(spriteloader$preparations);
            spriteloader$preparations.regions()
                .forEach((p_465760_, p_465761_) -> output.put(new Material(this.entry.config.textureId, p_465760_), p_465761_));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class PendingStitchResults {
        final List<AtlasManager.PendingStitch> pendingStitches;
        private final Map<Identifier, CompletableFuture<SpriteLoader.Preparations>> stitchFuturesById;
        final CompletableFuture<?> allReadyToUpload;

        PendingStitchResults(
            List<AtlasManager.PendingStitch> pendingStitches, Map<Identifier, CompletableFuture<SpriteLoader.Preparations>> stitchFutureById, CompletableFuture<?> allReadyToUpload
        ) {
            this.pendingStitches = pendingStitches;
            this.stitchFuturesById = stitchFutureById;
            this.allReadyToUpload = allReadyToUpload;
        }

        public Map<Material, TextureAtlasSprite> joinAndUpload() {
            Map<Material, TextureAtlasSprite> map = new HashMap<>();
            this.pendingStitches.forEach(p_433127_ -> p_433127_.joinAndUpload(map));
            return map;
        }

        public CompletableFuture<SpriteLoader.Preparations> get(Identifier id) {
            return Objects.requireNonNull(this.stitchFuturesById.get(id));
        }
    }
}
