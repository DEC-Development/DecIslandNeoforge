// 基岩版生物 → Java 版（NeoForge 1.21.1）批量移植生成器
// 产出：geometry/animation 资源、贴图 PNG、loot table、biome modifier、注册 Kotlin 代码
import fs from 'fs';
import path from 'path';
import zlib from 'zlib';

const BP = 'C:/Users/Administrator/Desktop/bedrock/DecIslandB';
const RP = 'C:/Users/Administrator/Desktop/bedrock/DecIslandR';
const PROJ = 'f:/DecIslandNeoforge-main';
const RES = `${PROJ}/src/main/resources`;
const SRC = `${PROJ}/src/main/java/com/dec/decisland`;

const log = { warn: [], info: [] };
const warn = (m) => { log.warn.push(m); };

// 基岩版 JSON 允许注释/尾逗号 → 剥离后再解析
function readJson(p) {
  let t = fs.readFileSync(p, 'utf8');
  let out = '';
  let inStr = false, esc = false;
  for (let i = 0; i < t.length; i++) {
    const c = t[i], n = t[i + 1];
    if (inStr) {
      out += c;
      if (esc) esc = false;
      else if (c === '\\') esc = true;
      else if (c === '"') inStr = false;
      continue;
    }
    if (c === '"') { inStr = true; out += c; continue; }
    if (c === '/' && n === '/') { while (i < t.length && t[i] !== '\n') i++; out += '\n'; continue; }
    if (c === '/' && n === '*') { i += 2; while (i < t.length && !(t[i] === '*' && t[i + 1] === '/')) i++; i++; continue; }
    out += c;
  }
  out = out.replace(/,(\s*[}\]])/g, '$1');
  return JSON.parse(out);
}

// ============ 移植清单 ============
const MOB_SKIP = new Set(['elf_of_leaves', 'zombie_warrior', 'leaves_golem', 'dragon_head', 'host_of_deep_1', 'host_of_deep_2']);
const MOB_SIMPLE_KEEP = new Set([
  'ash_bomb', 'ash_horse_head', 'ash_knight_head', 'ash_sword', 'ash_sword_phantom', 'bat_charger',
  'blackstone_thorn', 'ender_snake', 'friendly_spider', 'frozen_heart', 'frozen_heart_sleeping',
  'frozen_shadow', 'frozen_spider', 'ghost_diamond_sword', 'ghost_iron_sword', 'ghost_wooden_sword',
  'ice_thorn', 'nether_phantom', 'saucer', 'murloc_wizard', 'everlasting_winter_shadow',
]);
const VANILLA_KEEP = new Set([
  'angry_chicken', 'ash_pufferfish', 'baby_ender_dragon', 'blood_zombie', 'brown_bear', 'crimson_slime',
  'elf_of_deep', 'enchant_illager', 'enchant_illager_1', 'enchant_illager_2', 'end_stone_golem',
  'everlasting_winter_ghast_1', 'fire_storm', 'frozen_ghast', 'gargoyle', 'ice_bat', 'ice_creeper',
  'jungle_spider', 'little_bat', 'murloc', 'nether_creeper', 'nether_golem', 'nether_skeleton',
  'obsidian_golem', 'polar_wolf', 'predators', 'pumpkin_slime', 'radiate_creeper', 'radiate_enderman',
  'radiate_spider', 'real_soul', 'sardine', 'shadow_archer', 'shadow_creeper', 'shadow_skeleton',
  'shadow_skeleton_puppet', 'simple_glider', 'son_of_chaos', 'son_of_nature', 'soul', 'soul_blaze',
  'star', 'stone_golem', 'swamp_golem', 'vampire_bat', 'vampire_bat_by_boss', 'warped_skeleton', 'watcher',
]);
const SPECIAL_KEEP = new Set(['clam', 'crab', 'zombie_fish', 'mushroom_monster']);
const PORTED = new Set(['elf_of_leaves', 'zombie_warrior', 'leaves_golem', 'pumpkin_bomb']);

function buildPortList() {
  const entsDir = `${BP}/entities`;
  const names = fs.readdirSync(entsDir).filter((f) => f.endsWith('.json')).map((f) => f.slice(0, -5));
  const keep = [];
  for (const n of names) {
    if (PORTED.has(n)) continue;
    if (n.endsWith('_tp_anchor') || n.endsWith('_anchor')) continue;
    if (/^event_/.test(n)) continue;
    if (['group', 'height_judge', 'illusioner_blindness', 'scarecrow', 'shadow_crystal', 'radiate_crystal',
      'everlasting_winter_crystal', 'shadow_fuse', 'shadow_portal', 'village_portal', 'simple_car',
      'escaped_soul', 'escaped_soul_1', 'escaped_soul_2', 'escaped_soul_3', 'empty', 'gravestone',
      'mine', 'snow_energy_fuse', 'storm_energy', 'frozen_laser_small'].includes(n)) continue;
    const j = readJson(`${entsDir}/${n}.json`);
    const desc = j['minecraft:entity']?.description ?? {};
    if (desc.runtime_identifier?.startsWith('minecraft:') && !desc.runtime_identifier.startsWith('minecraft:') === false) {
      // 纯原版覆盖（无 dec: 前缀 identifier）跳过
      if (!String(desc.identifier).startsWith('dec:')) continue;
    }
    const runtime = desc.runtime_identifier ?? '-';
    const isDec = String(desc.identifier).startsWith('dec:');
    if (!isDec) continue;
    // PROJECTILE 组（运行时为投射物且在 PROJECTILE 分类中）跳过 —— 已大多移植
    if (VANILLA_KEEP.has(n) || SPECIAL_KEEP.has(n) || MOB_SIMPLE_KEEP.has(n)) { keep.push(n); continue; }
    if (MOB_SKIP.has(n)) continue;
    // 投射物特征：runtime snowball/egg/arrow/fireball/llama_spit/eye_of_ender/tnt/fishing_hook/small_fireball/dragon_fireball
    if (/^minecraft:(snowball|egg|arrow|fireball|small_fireball|dragon_fireball|llama_spit|eye_of_ender|splash_potion|fishing_hook|tnt|armor_stand|leash_knot|xporb|thrown_trident)$/.test(runtime)) continue;
    keep.push(n);
  }
  return keep.sort();
}

// ============ 原版 runtime → Java 类映射 ============
const VANILLA_MAP = {
  'minecraft:zombie': { cls: 'Zombie', pkg: 'monster', ren: 'ZombieRenderer', rp: 'Zombie', pkgR: 'monster', human64: true },
  'minecraft:husk': { cls: 'Zombie', pkg: 'monster', ren: 'ZombieRenderer', rp: 'Zombie', pkgR: 'monster', human64: true },
  'minecraft:drowned': { cls: 'Drowned', pkg: 'monster', ren: 'DrownedRenderer', rp: 'Drowned', pkgR: 'monster', human64: true },
  'minecraft:skeleton': { cls: 'Skeleton', pkg: 'monster', ren: 'SkeletonRenderer', rp: 'AbstractSkeleton', pkgR: 'monster', human64: true },
  'minecraft:stray': { cls: 'Stray', pkg: 'monster', ren: 'StrayRenderer', rp: 'Stray', pkgR: 'monster', human64: true },
  'minecraft:wither_skeleton': { cls: 'WitherSkeleton', pkg: 'monster', ren: 'WitherSkeletonRenderer', rp: 'WitherSkeleton', pkgR: 'monster', human64: true },
  'minecraft:creeper': { cls: 'Creeper', pkg: 'monster', ren: 'CreeperRenderer', rp: 'Creeper', pkgR: 'monster' },
  'minecraft:bat': { cls: 'Bat', pkg: 'ambient', ren: 'BatRenderer', rp: 'Bat', pkgR: 'ambient' },
  'minecraft:ghast': { cls: 'Ghast', pkg: 'monster', ren: 'GhastRenderer', rp: 'Ghast', pkgR: 'monster', flying: true },
  'minecraft:blaze': { cls: 'Blaze', pkg: 'monster', ren: 'BlazeRenderer', rp: 'Blaze', pkgR: 'monster', fire: true },
  'minecraft:slime': { cls: 'Slime', pkg: 'monster', ren: 'SlimeRenderer', rp: 'Slime', pkgR: 'monster' },
  'minecraft:magma_cube': { cls: 'MagmaCube', pkg: 'monster', ren: 'MagmaCubeRenderer', rp: 'MagmaCube', pkgR: 'monster', fire: true },
  'minecraft:vex': { cls: 'Vex', pkg: 'monster', ren: 'VexRenderer', rp: 'Vex', pkgR: 'monster', flying: true },
  'minecraft:guardian': { cls: 'Guardian', pkg: 'monster', ren: 'GuardianRenderer', rp: 'Guardian', pkgR: 'monster', water: true },
  'minecraft:iron_golem': { cls: 'IronGolem', pkg: 'animal', ren: 'IronGolemRenderer', rp: 'IronGolem', pkgR: 'animal' },
  'minecraft:snow_golem': { cls: 'SnowGolem', pkg: 'animal', ren: 'SnowGolemRenderer', rp: 'SnowGolem', pkgR: 'animal' },
  'minecraft:polar_bear': { cls: 'PolarBear', pkg: 'animal', ren: 'PolarBearRenderer', rp: 'PolarBear', pkgR: 'animal' },
  'minecraft:cave_spider': { cls: 'CaveSpider', pkg: 'monster', ren: 'CaveSpiderRenderer', rp: 'CaveSpider', pkgR: 'monster' },
  'minecraft:spider': { cls: 'Spider', pkg: 'monster', ren: 'SpiderRenderer', rp: 'Spider', pkgR: 'monster' },
  'minecraft:enderman': { cls: 'EnderMan', pkg: 'monster', ren: 'EndermanRenderer', rp: 'EnderMan', pkgR: 'monster' },
  'minecraft:cod': { cls: 'Cod', pkg: 'animal', ren: 'CodRenderer', rp: 'Cod', pkgR: 'animal', water: true },
  'minecraft:chicken': { cls: 'Chicken', pkg: 'animal', ren: 'ChickenRenderer', rp: 'Chicken', pkgR: 'animal' },
  'minecraft:evocation_illager': { cls: 'Evoker', pkg: 'monster', ren: 'EvokerRenderer', rp: 'SpellcasterIllager', pkgR: 'monster', human64: true },
  'minecraft:silverfish': { cls: 'Silverfish', pkg: 'monster', ren: 'SilverfishRenderer', rp: 'Silverfish', pkgR: 'monster' },
  'minecraft:sliverfish': { cls: 'Silverfish', pkg: 'monster', ren: 'SilverfishRenderer', rp: 'Silverfish', pkgR: 'monster' },
  'minecraft:wolf': { cls: 'Wolf', pkg: 'animal', ren: 'WolfRenderer', rp: 'Wolf', pkgR: 'animal' },
  'minecraft:pillager': { cls: 'Pillager', pkg: 'monster', ren: 'PillagerRenderer', rp: 'Pillager', pkgR: 'monster', human64: true },
  'minecraft:vindicator': { cls: 'Vindicator', pkg: 'monster', ren: 'VindicatorRenderer', rp: 'Vindicator', pkgR: 'monster', human64: true },
  'minecraft:phantom': { cls: 'Phantom', pkg: 'monster', ren: 'PhantomRenderer', rp: 'Phantom', pkgR: 'monster', flying: true },
  'minecraft:cow': { cls: 'Cow', pkg: 'animal', ren: 'CowRenderer', rp: 'Cow', pkgR: 'animal' },
  'minecraft:villager': { cls: 'Villager', pkg: 'npc', ren: 'VillagerRenderer', rp: 'Villager', pkgR: 'npc' },
  'minecraft:witch': { cls: 'Witch', pkg: 'monster', ren: 'WitchRenderer', rp: 'Witch', pkgR: 'monster' },
  'minecraft:salmon': { cls: 'Salmon', pkg: 'animal', ren: 'SalmonRenderer', rp: 'Salmon', pkgR: 'animal', water: true },
  'minecraft:illusioner': { cls: 'Illusioner', pkg: 'monster', ren: 'IllusionerRenderer', rp: 'Illusioner', pkgR: 'monster' },
};
// 客户端几何体前缀 → 原版模型（无 runtime_identifier 时的回退判断）
// 注意：不能用 \b（下划线是 word 字符）；长名在前；rest 仅允许空/点分/特定后缀，防止 everlasting_winter_ghast 被误判为 ghast
const VANILLA_GEO_RE = /^geometry\.(villager\.witch|snowgolem_have_block|wither_skeleton|evocation_illager|polar_bear|polarbear|magma_cube|cave_spider|cavespider|illusioner|villager|silverfish|vindicator|irongolem|iron_golem|snow_golem|snowgolem|drowned|skeleton|husk|zombie|stray|creeper|ghast|blaze|slime|vex|guardian|spider|enderman|chicken|phantom|cow|witch|salmon|cod|wolf|pillager|bat)(.*)$/i;
function matchVanillaGeo(geoId) {
  if (!geoId) return null;
  const m = VANILLA_GEO_RE.exec(geoId);
  if (!m) return null;
  const rest = m[2];
  if (rest !== '' && !rest.startsWith('.') && rest !== '_have_block') return null;
  return GEO_PREFIX_TO_RUNTIME[m[1].toLowerCase()];
}
const GEO_PREFIX_TO_RUNTIME = {
  zombie: 'minecraft:zombie', husk: 'minecraft:husk', drowned: 'minecraft:drowned',
  skeleton: 'minecraft:skeleton', wither_skeleton: 'minecraft:wither_skeleton', stray: 'minecraft:stray',
  creeper: 'minecraft:creeper', bat: 'minecraft:bat', ghast: 'minecraft:ghast', blaze: 'minecraft:blaze',
  slime: 'minecraft:slime', magma_cube: 'minecraft:magma_cube', vex: 'minecraft:vex',
  guardian: 'minecraft:guardian', iron_golem: 'minecraft:iron_golem', irongolem: 'minecraft:iron_golem',
  snow_golem: 'minecraft:snow_golem', snowgolem: 'minecraft:snow_golem', snowgolem_have_block: 'minecraft:snow_golem',
  polarbear: 'minecraft:polar_bear', polar_bear: 'minecraft:polar_bear', spider: 'minecraft:spider',
  cavespider: 'minecraft:cave_spider', cave_spider: 'minecraft:cave_spider', enderman: 'minecraft:enderman',
  cod: 'minecraft:cod', salmon: 'minecraft:salmon', chicken: 'minecraft:chicken', evocation_illager: 'minecraft:evocation_illager',
  illusioner: 'minecraft:illusioner', silverfish: 'minecraft:silverfish', wolf: 'minecraft:wolf', pillager: 'minecraft:pillager',
  vindicator: 'minecraft:vindicator', phantom: 'minecraft:phantom', cow: 'minecraft:cow',
  villager: 'minecraft:villager', 'villager.witch': 'minecraft:witch', witch: 'minecraft:witch',
};
// 泛型渲染器 → 类型参数（用 renParam 实体类）
const GENERIC_REN_PARAM = { SkeletonRenderer: 'AbstractSkeleton', SpiderRenderer: 'Spider', EvokerRenderer: 'SpellcasterIllager' };
// 原版属性构建器（cls 无自身 createAttributes 时回退）
const ATTR_BUILDER = {
  Skeleton: 'net.minecraft.world.entity.monster.AbstractSkeleton.createAttributes()',
  Stray: 'net.minecraft.world.entity.monster.AbstractSkeleton.createAttributes()',
  WitherSkeleton: 'net.minecraft.world.entity.monster.AbstractSkeleton.createAttributes()',
  CaveSpider: 'net.minecraft.world.entity.monster.Spider.createAttributes()',
  Cod: 'net.minecraft.world.entity.animal.AbstractFish.createAttributes()',
  Salmon: 'net.minecraft.world.entity.animal.AbstractFish.createAttributes()',
  Slime: 'net.minecraft.world.entity.monster.Monster.createMonsterAttributes()',
  Phantom: 'net.minecraft.world.entity.FlyingMob.createMobAttributes().add(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE, 6.0)',
};
// 原版默认最大生命（用于判断是否覆盖基岩血量）
const VANILLA_HP = {
  Zombie: 20, Drowned: 20, Skeleton: 20, Stray: 20, WitherSkeleton: 20, Creeper: 20, Bat: 6, Ghast: 10,
  Blaze: 20, Slime: 16, MagmaCube: 16, Vex: 14, Guardian: 30, IronGolem: 100, SnowGolem: 4, PolarBear: 30,
  CaveSpider: 16, Spider: 16, EnderMan: 40, Cod: 3, Chicken: 4, Evoker: 24, Silverfish: 8, Wolf: 8,
  Pillager: 24, Vindicator: 24, Phantom: 20, Cow: 10, Villager: 20, Witch: 26, Salmon: 3, Illusioner: 32,
};

// ============ 生成群系标签映射 ============
const TAG_MAP = {
  taiga: ['minecraft:taiga', 'minecraft:snowy_taiga', 'minecraft:old_growth_pine_taiga', 'minecraft:old_growth_spruce_taiga'],
  mega_taiga: ['minecraft:old_growth_pine_taiga', 'minecraft:old_growth_spruce_taiga'],
  forest: ['minecraft:forest', 'minecraft:birch_forest', 'minecraft:dark_forest', 'minecraft:flower_forest', 'minecraft:old_growth_birch_forest'],
  plains: ['minecraft:plains', 'minecraft:sunflower_plains'],
  desert: ['minecraft:desert'],
  jungle: ['minecraft:jungle', 'minecraft:bamboo_jungle', 'minecraft:sparse_jungle'],
  savanna: ['minecraft:savanna', 'minecraft:savanna_plateau', 'minecraft:windswept_savanna'],
  swamp: ['minecraft:swamp', 'minecraft:mangrove_swamp'],
  mountains: ['minecraft:windswept_hills', 'minecraft:windswept_gravelly_hills', 'minecraft:windswept_forest', 'minecraft:jagged_peaks', 'minecraft:frozen_peaks', 'minecraft:stony_peaks', 'minecraft:snowy_slopes', 'minecraft:meadow', 'minecraft:grove'],
  mountain: ['minecraft:windswept_hills', 'minecraft:windswept_gravelly_hills', 'minecraft:windswept_forest', 'minecraft:jagged_peaks', 'minecraft:frozen_peaks', 'minecraft:stony_peaks', 'minecraft:snowy_slopes', 'minecraft:meadow', 'minecraft:grove'],
  extreme_hills: ['minecraft:windswept_hills', 'minecraft:windswept_gravelly_hills', 'minecraft:windswept_forest'],
  snowy: ['minecraft:snowy_plains', 'minecraft:ice_spikes', 'minecraft:snowy_beach', 'minecraft:snowy_taiga', 'minecraft:frozen_river'],
  frozen: ['minecraft:frozen_ocean', 'minecraft:deep_frozen_ocean', 'minecraft:frozen_river', 'minecraft:snowy_plains', 'minecraft:ice_spikes', 'minecraft:snowy_taiga', 'minecraft:frozen_peaks', 'minecraft:snowy_slopes'],
  cold: ['minecraft:cold_ocean', 'minecraft:deep_cold_ocean', 'minecraft:taiga', 'minecraft:snowy_plains', 'minecraft:snowy_taiga'],
  temperate: ['minecraft:plains', 'minecraft:forest', 'minecraft:birch_forest'],
  lukewarm: ['minecraft:lukewarm_ocean', 'minecraft:deep_lukewarm_ocean'],
  warm: ['minecraft:warm_ocean', 'minecraft:desert', 'minecraft:jungle', 'minecraft:savanna', 'minecraft:lukewarm_ocean', 'minecraft:deep_lukewarm_ocean'],
  ocean: ['minecraft:ocean', 'minecraft:deep_ocean', 'minecraft:warm_ocean', 'minecraft:lukewarm_ocean', 'minecraft:deep_lukewarm_ocean', 'minecraft:cold_ocean', 'minecraft:deep_cold_ocean', 'minecraft:frozen_ocean', 'minecraft:deep_frozen_ocean'],
  deep_ocean: ['minecraft:deep_ocean', 'minecraft:deep_lukewarm_ocean', 'minecraft:deep_cold_ocean', 'minecraft:deep_frozen_ocean'],
  river: ['minecraft:river', 'minecraft:frozen_river'],
  beach: ['minecraft:beach', 'minecraft:snowy_beach', 'minecraft:stony_shore'],
  nether: ['minecraft:nether_wastes', 'minecraft:crimson_forest', 'minecraft:warped_forest', 'minecraft:soul_sand_valley', 'minecraft:basalt_deltas'],
  nether_wastes: ['minecraft:nether_wastes'],
  crimson_forest: ['minecraft:crimson_forest'],
  warped_forest: ['minecraft:warped_forest'],
  soul_sand_valley: ['minecraft:soul_sand_valley'],
  soulsand_valley: ['minecraft:soul_sand_valley'],
  basalt_deltas: ['minecraft:basalt_deltas'],
  the_end: ['minecraft:the_end', 'minecraft:end_highlands', 'minecraft:end_midlands', 'minecraft:small_end_islands', 'minecraft:end_barrens'],
  end: ['minecraft:the_end', 'minecraft:end_highlands', 'minecraft:end_midlands', 'minecraft:small_end_islands', 'minecraft:end_barrens'],
  mushroom: ['minecraft:mushroom_fields'],
  mushroom_island: ['minecraft:mushroom_fields'],
  mushroom_fields: ['minecraft:mushroom_fields'],
  // 泛化标签（基岩对全体怪物/动物群系）→ 常规主世界群系（getter 延迟求值，避免 TDZ）
  get monster() { return DEFAULT_BIOMES; },
  get animal() { return DEFAULT_BIOMES; },
  deep_dark: ['minecraft:deep_dark'],
  mesa: ['minecraft:badlands', 'minecraft:wooded_badlands', 'minecraft:eroded_badlands'],
  badlands: ['minecraft:badlands', 'minecraft:wooded_badlands', 'minecraft:eroded_badlands'],
  flower_forest: ['minecraft:flower_forest'],
  birch_forest: ['minecraft:birch_forest'],
  dark_forest: ['minecraft:dark_forest'],
  sunflower_plains: ['minecraft:sunflower_plains'],
  ice_plains: ['minecraft:snowy_plains', 'minecraft:ice_spikes'],
  ice: ['minecraft:snowy_plains', 'minecraft:ice_spikes', 'minecraft:frozen_ocean', 'minecraft:frozen_peaks'],
};
const DEFAULT_BIOMES = ['minecraft:plains', 'minecraft:forest', 'minecraft:birch_forest', 'minecraft:dark_forest', 'minecraft:taiga', 'minecraft:desert', 'minecraft:savanna', 'minecraft:swamp', 'minecraft:snowy_plains', 'minecraft:windswept_hills'];
const OCEAN_BIOMES = ['minecraft:ocean', 'minecraft:deep_ocean', 'minecraft:lukewarm_ocean', 'minecraft:deep_lukewarm_ocean', 'minecraft:cold_ocean', 'minecraft:deep_cold_ocean', 'minecraft:frozen_ocean', 'minecraft:deep_frozen_ocean'];

// ============ 贴图：TGA 解码 + PNG 编码 ============
function decodeTGA(buf) {
  const idLength = buf[0];
  const imageType = buf[2];
  const width = buf.readUInt16LE(12);
  const height = buf.readUInt16LE(14);
  const bpp = buf[16];
  const descriptor = buf[17];
  const topDown = (descriptor & 0x20) !== 0;
  const bytesPP = bpp / 8;
  let off = 18 + idLength;
  if (imageType === 1 || imageType === 9) off += buf.readUInt16LE(4) * 2; // 跳过 colormap（罕见）
  const px = Buffer.alloc(width * height * 4);
  const setPx = (i, b, g, r, a) => { px[i * 4] = r; px[i * 4 + 1] = g; px[i * 4 + 2] = b; px[i * 4 + 3] = a; };
  const bytes = width * height * bytesPP;
  const data = buf.subarray(off, off + bytes);

  if (imageType === 2 || imageType === 3) {
    let p = 0;
    for (let i = 0; i < width * height; i++) {
      if (imageType === 3) setPx(i, data[p], data[p], data[p], 255), (p += 1);
      else if (bytesPP === 4) setPx(i, data[p], data[p + 1], data[p + 2], data[p + 3]), (p += 4);
      else setPx(i, data[p], data[p + 1], data[p + 2], 255), (p += 3);
    }
  } else if (imageType === 10 || imageType === 11) {
    let p = 0, i = 0;
    while (i < width * height && p < data.length) {
      const h = data[p++];
      const count = (h & 0x7f) + 1;
      const read = () => { if (imageType === 11) { const g = data[p]; p += 1; return [g, g, g, 255]; } if (bytesPP === 4) { const v = [data[p], data[p + 1], data[p + 2], data[p + 3]]; p += 4; return v; } const v = [data[p], data[p + 1], data[p + 2], 255]; p += 3; return v; };
      if (h & 0x80) { const c = read(); for (let k = 0; k < count; k++, i++) setPx(i, c[0], c[1], c[2], c[3]); }
      else for (let k = 0; k < count; k++, i++) { const c = read(); setPx(i, c[0], c[1], c[2], c[3]); }
    }
  } else {
    return null;
  }
  // 行序：TGA 默认自底向上 → 翻转为自顶向下
  if (!topDown) {
    const row = Buffer.alloc(width * 4);
    for (let y = 0; y < height / 2; y++) {
      const a = y * width * 4, b = (height - 1 - y) * width * 4;
      data.copy ? null : null;
      px.copy(row, 0, a, a + width * 4);
      px.copy(px, a, b, b + width * 4);
      row.copy(px, b);
    }
  }
  return { width, height, px };
}

function encodePNG(width, height, rgba) {
  const raw = Buffer.alloc((width * 4 + 1) * height);
  for (let y = 0; y < height; y++) {
    raw[y * (width * 4 + 1)] = 0;
    rgba.copy(raw, y * (width * 4 + 1) + 1, y * width * 4, (y + 1) * width * 4);
  }
  const chunk = (type, data) => {
    const len = Buffer.alloc(4); len.writeUInt32BE(data.length);
    const body = Buffer.concat([Buffer.from(type), data]);
    const crc = Buffer.alloc(4); crc.writeUInt32BE(zlib.crc32(body) >>> 0);
    return Buffer.concat([len, body, crc]);
  };
  const ihdr = Buffer.alloc(13);
  ihdr.writeUInt32BE(width, 0); ihdr.writeUInt32BE(height, 4);
  ihdr[8] = 8; ihdr[9] = 6; // 8bit RGBA
  return Buffer.concat([
    Buffer.from([0x89, 0x50, 0x4e, 0x47, 0x0d, 0x0a, 0x1a, 0x0a]),
    chunk('IHDR', ihdr), chunk('IDAT', zlib.deflateSync(raw)), chunk('IEND', Buffer.alloc(0)),
  ]);
}

// 基岩 64×32 旧布局 → Java 64×64 布局（人形外层 UV 复制到下半区）
function legacyHumanoidTo64(img) {
  if (img.width !== 64 || img.height !== 32) return img;
  const out = Buffer.alloc(64 * 64 * 4, 0);
  img.px.copy(out, 0); // 上半区原样
  const copy = (sx, sy, w, h, dx, dy) => {
    for (let y = 0; y < h; y++) {
      img.px.copy(out, ((dy + y) * 64 + dx) * 4, ((sy + y) * 64 + sx) * 4, ((sy + y) * 64 + sx + w) * 4);
    }
  };
  copy(16, 16, 24, 16, 16, 32); // jacket
  copy(40, 16, 16, 16, 40, 32); // right sleeve
  copy(40, 16, 16, 16, 40, 48); // left sleeve
  copy(0, 16, 16, 16, 0, 32);   // right pants
  copy(0, 16, 16, 16, 0, 48);   // left pants
  return { width: 64, height: 64, px: out };
}

function findTexture(rel) {
  if (!rel) return null;
  const clean = rel.replace(/^textures\//, '');
  for (const ext of ['.tga', '.png', '.jpg', '.jpeg']) {
    const p = `${RP}/textures/${clean}${ext}`;
    if (fs.existsSync(p)) return p;
  }
  // 子目录模糊匹配（末段同名）
  const base = path.basename(clean);
  const hit = [];
  (function walk(d) {
    for (const f of fs.readdirSync(d)) {
      const p = `${d}/${f}`;
      if (fs.statSync(p).isDirectory()) walk(p);
      else if (f.startsWith(base + '.')) hit.push(p);
    }
  })(`${RP}/textures`);
  return hit.find((p) => p.endsWith('.tga')) ?? hit[0] ?? null;
}

function convertTexture(srcPath, outName, human64) {
  const outDir = `${RES}/assets/decisland/textures/entity`;
  fs.mkdirSync(outDir, { recursive: true });
  const outP = `${outDir}/${outName}.png`;
  if (srcPath.endsWith('.png') || srcPath.endsWith('.jpg')) {
    fs.copyFileSync(srcPath, outP);
    return 'copy';
  }
  const img = decodeTGA(fs.readFileSync(srcPath));
  if (!img) { warn(`贴图解码失败: ${srcPath}`); return null; }
  const fixed = human64 ? legacyHumanoidTo64(img) : img;
  fs.writeFileSync(outP, encodePNG(fixed.width, fixed.height, fixed.px));
  return 'convert';
}

// ============ 刷怪蛋颜色：贴图平均色 + 名字哈希兜底 ============
function decodePNG(buf) {
  // 极简 PNG 解码：仅支持 8bit RGB(2)/RGBA(6) + filter 0-4（实体贴图均为此格式）
  if (buf.length < 8 || buf.readUInt32BE(0) !== 0x89504e47) return null;
  let pos = 8, w = 0, h = 0, bpp = 0;
  const idat = [];
  while (pos + 8 <= buf.length) {
    const len = buf.readUInt32BE(pos), type = buf.toString('ascii', pos + 4, pos + 8);
    const data = buf.subarray(pos + 8, pos + 8 + len);
    if (type === 'IHDR') {
      w = data.readUInt32BE(0); h = data.readUInt32BE(4);
      if (data[8] !== 8) return null;
      bpp = data[9] === 6 ? 4 : (data[9] === 2 ? 3 : 0);
      if (!bpp) return null;
    } else if (type === 'IDAT') idat.push(data);
    else if (type === 'IEND') break;
    pos += 12 + len;
  }
  if (!w || !h || !idat.length) return null;
  let raw;
  try { raw = zlib.inflateSync(Buffer.concat(idat)); } catch { return null; }
  const stride = w * bpp, out = Buffer.alloc(w * h * 4);
  let prev = Buffer.alloc(stride);
  for (let y = 0; y < h; y++) {
    const f = raw[y * (stride + 1)], line = raw.subarray(y * (stride + 1) + 1, (y + 1) * (stride + 1));
    const cur = Buffer.alloc(stride);
    for (let i = 0; i < stride && i < line.length; i++) {
      const a = i >= bpp ? cur[i - bpp] : 0, b = prev[i], c = i >= bpp ? prev[i - bpp] : 0;
      let v = line[i];
      if (f === 1) v = (v + a) & 0xff;
      else if (f === 2) v = (v + b) & 0xff;
      else if (f === 3) v = (v + ((a + b) >> 1)) & 0xff;
      else if (f === 4) {
        const p = a + b - c, pa = Math.abs(p - a), pb = Math.abs(p - b), pc = Math.abs(p - c);
        v = (v + (pa <= pb && pa <= pc ? a : pb <= pc ? b : c)) & 0xff;
      }
      cur[i] = v;
    }
    prev = cur;
    for (let x = 0; x < w; x++) {
      const s = x * bpp, d = (y * w + x) * 4;
      if (bpp === 4) cur.copy(out, d, s, s + 4);
      else { out[d] = cur[s]; out[d + 1] = cur[s + 1]; out[d + 2] = cur[s + 2]; out[d + 3] = 255; }
    }
  }
  return { width: w, height: h, px: out };
}

function hslToRgb(hue, sat, lit) {
  const f = (n) => {
    const k = (n + hue * 12) % 12, a = sat * Math.min(lit, 1 - lit);
    return Math.round(255 * (lit - a * Math.max(-1, Math.min(k - 3, 9 - k, 1))));
  };
  return [f(0), f(8), f(4)];
}

function eggColorsFor(name, pngPath) {
  // 优先取实体贴图平均色（跳过透明像素）；无贴图/解码失败 → 名字 FNV 哈希出稳定色相
  let rgb = null;
  try {
    const img = decodePNG(fs.readFileSync(pngPath));
    if (img) {
      let r = 0, g = 0, b = 0, n = 0;
      for (let i = 0; i < img.px.length; i += 4) {
        if (img.px[i + 3] < 32) continue;
        r += img.px[i]; g += img.px[i + 1]; b += img.px[i + 2]; n++;
      }
      if (n >= 16) rgb = [r / n, g / n, b / n];
    }
  } catch { /* 无文件 → 哈希兜底 */ }
  if (!rgb) {
    let hsh = 2166136261;
    for (const ch of name) { hsh ^= ch.charCodeAt(0); hsh = Math.imul(hsh, 16777619) >>> 0; }
    rgb = hslToRgb((hsh % 360) / 360, 0.55, 0.45);
  }
  const [r, g, b] = rgb;
  const cl = (v) => Math.min(255, Math.round(v));
  return {
    bg: (Math.round(r) << 16) | (Math.round(g) << 8) | Math.round(b),
    hi: (cl(r * 1.35 + 18) << 16) | (cl(g * 1.35 + 18) << 8) | cl(b * 1.35 + 18),
  };
}

// ============ 行为解析 ============
function parseBehavior(name) {
  const p = `${BP}/entities/${name}.json`;
  if (!fs.existsSync(p)) return null;
  const j = readJson(p);
  const e = j['minecraft:entity'];
  // 该基岩包部分文件用无前缀键（components/component_groups），兼容两种写法
  const components = e['minecraft:components'] ?? e.components ?? {};
  const groups = e['minecraft:component_groups'] ?? e.component_groups ?? {};
  // 合并 component_groups 中的行为组件（事件触发的组也参与 AI 推断）
  const merged = { ...components };
  const goalKeys = (obj) => Object.keys(obj).filter((k) => k.startsWith('minecraft:behavior.'));
  const mergeComponent = (k) => {
    if (merged[k] !== undefined) return;
    for (const g of Object.values(groups)) {
      if (g[k] !== undefined) { merged[k] = g[k]; return; }
    }
  };
  for (const g of Object.values(groups)) for (const k of goalKeys(g)) mergeComponent(k);

  const families = (merged['minecraft:type_family']?.family ?? []).map(String);
  // 数值提取：基岩允许区间对象（{range_min,range_max} / {min,max}）与表达式字符串，取有效值否则回退
  const numOr = (v, def) => {
    if (typeof v === 'number' && isFinite(v)) return v;
    if (v && typeof v === 'object') {
      const hi = [v.range_max, v.max, v.upper].find((x) => typeof x === 'number' && isFinite(x));
      if (hi != null) return hi;
      const lo = [v.range_min, v.min, v.lower].find((x) => typeof x === 'number' && isFinite(x));
      if (lo != null) return lo;
    }
    if (typeof v === 'string') { const n = parseFloat(v); if (isFinite(n)) return n; }
    return def;
  };
  const health = numOr(merged['minecraft:health']?.max, numOr(merged['minecraft:health']?.value, 20));
  const movement = numOr(merged['minecraft:movement']?.value, 0.25);
  const attack = numOr(merged['minecraft:attack']?.damage, 3);
  const scale = numOr(merged['minecraft:scale']?.value, 1);
  const box = merged['minecraft:collision_box'] ?? {};
  const kb = numOr(merged['minecraft:knockback_resistance']?.value, numOr(merged['minecraft:knockback_resistance'], 0));
  const follow = numOr(merged['minecraft:follow_range']?.value, numOr(merged['minecraft:follow_range'], 32));
  const fireImmune = merged['minecraft:fire_immune'] !== undefined || families.includes('fire_immune');

  const melee = merged['minecraft:behavior.melee_attack'] !== undefined || merged['minecraft:behavior.melee_box_attack'] !== undefined;
  const rangedComp = merged['minecraft:behavior.ranged_attack'] ?? merged['minecraft:shooter'] ?? null;
  const projectileDef = merged['minecraft:projectile']?.def ?? merged['minecraft:projectile']?.projectile
    ?? merged['minecraft:shooter']?.def ?? null;
  const ranged = rangedComp !== undefined && rangedComp !== null;
  const rangedInterval = Math.round(1 / (rangedComp?.interval ?? 0.025));

  // 目标推断
  const targets = new Set();
  const nat = merged['minecraft:behavior.nearest_attackable_target'];
  const collectTargets = (obj) => {
    if (!obj) return;
    for (const et of obj.entity_types ?? []) {
      const filters = JSON.stringify(et.filters ?? {});
      if (/player/.test(filters)) targets.add('player');
      if (/villager(?!_)/.test(filters)) targets.add('villager');
      if (/iron_golem|irongolem/.test(filters)) targets.add('iron_golem');
      if (/snow_golem|snowgolem/.test(filters)) targets.add('snow_golem');
      const val = typeof et.filters?.value === 'string' ? et.filters.value : null;
      if (val) targets.add(val.replace(/^minecraft:/, '').replace('irongolem', 'iron_golem'));
    }
  };
  collectTargets(nat);
  for (const g of Object.values(groups)) collectTargets(g['minecraft:behavior.nearest_attackable_target']);
  if (targets.size === 0 && (melee || ranged)) targets.add('player');

  const panic = merged['minecraft:behavior.panic'] !== undefined;
  const hostile = melee || ranged || nat !== undefined;
  const water = families.some((f) => /fish|water|aquatic|clam|crab|nautilus|urchin|sardine/.test(f))
    || merged['minecraft:underwater_movement'] !== undefined
    || merged['minecraft:behavior.swim_wander'] !== undefined
    || merged['minecraft:navigation.swim'] !== undefined;
  const undead = families.includes('undead');
  const flying = merged['minecraft:navigation.fly'] !== undefined || merged['minecraft:movement.fly'] !== undefined
    || merged['minecraft:behavior.random_hover'] !== undefined;

  return {
    name, components: merged, families, health, movement, attack, scale, box, kb, follow, fireImmune,
    melee, ranged, rangedInterval, projectileDef, targets: [...targets], panic, hostile, water, undead, flying,
    xp: 5,
  };
}

// ============ 客户端实体解析 ============
let animIndex = null;
function buildAnimIndex() {
  const idx = {};
  (function walk(d) {
    for (const f of fs.readdirSync(d)) {
      const p = `${d}/${f}`;
      if (fs.statSync(p).isDirectory()) walk(p);
      else if (f.endsWith('.json')) {
        try {
          const j = readJson(p);
          const anims = j.animations ?? {};
          for (const id of Object.keys(anims)) idx[id] = { file: p, obj: anims[id] };
        } catch { /* skip */ }
      }
    }
  })(`${RP}/animations`);
  return idx;
}

function parseClientEntity(name) {
  for (const cand of [`${RP}/entity/${name}.entity.json`, `${RP}/entity/${name}.json`]) {
    if (fs.existsSync(cand)) {
      const j = readJson(cand);
      const d = j['minecraft:client_entity']?.description ?? {};
      const texField = d.textures?.default ?? Object.values(d.textures ?? {})[0] ?? null;
      return { geometryId: d.geometry?.default ?? null, texturePath: texField, animations: d.animations ?? {} };
    }
  }
  return { geometryId: null, texturePath: null, animations: {} };
}

// ============ 物品ID清单（战利品校验） ============
// 基岩旧版物品名 → 1.21.1 现名
const ITEM_RENAME = {
  fish: 'cod', cooked_fish: 'cooked_cod', skull: 'skeleton_skull', waterlily: 'lily_pad',
  quartz_ore: 'nether_quartz_ore', red_flower: 'poppy', magma: 'magma_block', clownfish: 'tropical_fish',
  record: 'music_disc_13',
  record_13: 'music_disc_13', record_cat: 'music_disc_cat', record_blocks: 'music_disc_blocks',
  record_chirp: 'music_disc_chirp', record_far: 'music_disc_far', record_mall: 'music_disc_mall',
  record_mellohi: 'music_disc_mellohi', record_stal: 'music_disc_stal', record_strad: 'music_disc_strad',
  record_ward: 'music_disc_ward', record_11: 'music_disc_11', record_wait: 'music_disc_wait',
};

function collectItemIds() {
  const ids = new Set();
  // dec 物品：宽松扫描全部 Kotlin 源码中的引号 snake_case 字符串
  // （物品定义形式多样：registerMaterial("x")、列表元素 "x"、Builder("x")…校验池宁多勿缺）
  (function walk(d) {
    for (const f of fs.readdirSync(d)) {
      const p = `${d}/${f}`;
      const s = fs.statSync(p);
      if (s.isDirectory()) walk(p);
      else if (f.endsWith('.kt')) {
        const t = fs.readFileSync(p, 'utf8');
        for (const m of t.matchAll(/"([a-z][a-z0-9_]{2,})"/g)) ids.add(m[1]);
      }
    }
  })(SRC);
  // 原版物品：客户端 jar 提取的完整注册表（jar_extract.mjs 产出，含全部方块物品）
  const vanillaItems = `${PROJ}/tools/entity_port/vanilla_items.json`;
  if (fs.existsSync(vanillaItems)) {
    for (const i of JSON.parse(fs.readFileSync(vanillaItems, 'utf8'))) ids.add('mc:' + i);
  } else {
    throw new Error('缺少 vanilla_items.json —— 先运行 jar_extract.mjs');
  }
  return ids;
}

const EXTRA_VANILLA = new Set(['minecraft:iron_ingot', 'minecraft:gold_ingot', 'minecraft:emerald', 'minecraft:diamond',
  'minecraft:bone', 'minecraft:string', 'minecraft:gunpowder', 'minecraft:rotten_flesh', 'minecraft:arrow',
  'minecraft:torch', 'minecraft:carrot', 'minecraft:potato', 'minecraft:beetroot', 'minecraft:coal', 'minecraft:iron_nugget',
  'minecraft:gold_nugget', 'minecraft:redstone', 'minecraft:lapis_lazuli', 'minecraft:quartz', 'minecraft:leather',
  'minecraft:paper', 'minecraft:book', 'minecraft:glowstone_dust', 'minecraft:blaze_powder', 'minecraft:blaze_rod',
  'minecraft:ender_pearl', 'minecraft:ender_eye', 'minecraft:ghast_tear', 'minecraft:slime_ball', 'minecraft:snowball',
  'minecraft:stick', 'minecraft:apple', 'minecraft:bread', 'minecraft:wheat', 'minecraft:seagrass', 'minecraft:cod',
  'minecraft:salmon', 'minecraft:tropical_fish', 'minecraft:prismarine_shard', 'minecraft:prismarine_crystals',
  'minecraft:spider_eye', 'minecraft:sugar', 'minecraft:egg', 'minecraft:feather', 'minecraft:flint',
  'minecraft: experience_bottle'.trim(), 'minecraft:glass_bottle', 'minecraft:scute', 'minecraft:ink_sac',
  'minecraft:glow_ink_sac', 'minecraft:honeycomb', 'minecraft:cobweb', 'minecraft:clay_ball', 'minecraft:brick',
  'minecraft:nether_wart', 'minecraft:crying_obsidian', 'minecraft:obsidian', 'minecraft:cryingobsidian_placeholder'].map((s) => s.trim()));

function convertLoot(name) {
  const p = `${BP}/loot_tables/entities/${name}.json`;
  if (!fs.existsSync(p)) return { written: false };
  const outDir = `${RES}/data/decisland/loot_table/entities`;
  fs.mkdirSync(outDir, { recursive: true });

  const mapId = (id) => {
    if (typeof id !== 'string') return null;
    if (id.startsWith('dec:')) return `decisland:${id.slice(4)}`;
    if (id.startsWith('minecraft:')) {
      const short = id.slice(10);
      return ITEM_RENAME[short] ? `minecraft:${ITEM_RENAME[short]}` : id;
    }
    return id;
  };
  const itemValid = (javaId) => {
    if (!javaId?.startsWith('decisland:')) return ITEM_IDS.has('mc:' + javaId.slice(10)) || EXTRA_VANILLA.has(javaId) || javaId === 'minecraft:air';
    return ITEM_IDS.has(javaId.slice(10));
  };
  const convCount = (c) => {
    if (typeof c === 'number') return c;
    if (c && typeof c === 'object' && ('min' in c || 'max' in c)) return { min: c.min ?? 1, max: c.max ?? 1 };
    return c;
  };
  const dropped = [];
  const convFunctions = (fns) => (fns ?? []).map((f) => {
    const fn = f.function?.replace(/^minecraft:/, '');
    if (fn === 'set_count') return { function: 'minecraft:set_count', count: convCount(f.count) };
    if (fn === 'set_damage') return { function: 'minecraft:set_damage', damage: f.damage ?? { min: 0, max: 1 } };
    if (fn === 'looting_enchant') return { function: 'minecraft:enchanted_count_increase', enchantment: 'minecraft:looting', count: convCount(f.count ?? 1) };
    if (fn === 'enchant_randomly') return { function: 'minecraft:enchant_randomly' };
    if (fn === 'furnace_smelt') return { function: 'minecraft:furnace_smelt' };
    if (fn === 'set_name') return { function: 'minecraft:set_name', name: f.name };
    dropped.push(fn);
    return null;
  }).filter(Boolean);
  const convConditions = (conds) => (conds ?? []).map((c) => {
    const cc = c.condition?.replace(/^minecraft:/, '');
    if (cc === 'killed_by_player') return { condition: 'minecraft:killed_by_player' };
    if (cc === 'random_chance') return { condition: 'minecraft:random_chance', chance: c.chance };
    if (cc === 'random_chance_with_looting') return { condition: 'minecraft:random_chance', chance: c.chance ?? 0.05 };
    if (cc === 'random_regional_difficulty_chance') return null;
    dropped.push(cc);
    return null;
  }).filter(Boolean);

  const convEntry = (e, depth = 0) => {
    const type = (e.type ?? 'item').replace(/^minecraft:/, '');
    if (type === 'item') {
      const javaId = mapId(e.name);
      if (!javaId || !itemValid(javaId)) { dropped.push('item:' + e.name); return null; }
      const out = { type: 'minecraft:item', name: javaId };
      if (e.weight != null) out.weight = e.weight;
      if (e.quality != null) out.quality = e.quality;
      if (e.functions?.length) out.functions = convFunctions(e.functions);
      if (e.conditions?.length) out.conditions = convConditions(e.conditions);
      if (e.children?.length) {
        const kids = (e.children ?? []).map((k) => convEntry(k, depth)).filter(Boolean);
        if (kids.length) out.children = kids;
      }
      return out;
    }
    if (type === 'empty') return { type: 'minecraft:empty' };
    if (type === 'loot_table') {
      // 子表引用：dec 本地表 → 递归内联；原版表 → 映射 minecraft: 命名空间
      const ref = String(e.name ?? '');
      const rel = ref.replace(/^loot_tables\//, '').replace(/\.json$/, '');
      const local = `${BP}/loot_tables/${rel}.json`;
      if (depth < 4 && fs.existsSync(local)) {
        try {
          const sub = readJson(local);
          const kids = (sub.pools ?? []).flatMap((pl) => (pl.entries ?? []).map((k) => convEntry(k, depth + 1))).filter(Boolean);
          if (kids.length) return { type: 'minecraft:group', children: kids };
        } catch { /* fallthrough */ }
        dropped.push('loot_table(空):' + ref);
        return null;
      }
      return { type: 'minecraft:loot_table', name: `minecraft:${rel}` };
    }
    dropped.push('type:' + type);
    return null;
  };

  const src = readJson(p);
  const pools = [];
  for (const pool of src.pools ?? []) {
    const entries = (pool.entries ?? []).map(convEntry).filter(Boolean);
    if (!entries.length) continue;
    const out = { rolls: pool.rolls ?? 1 };
    if (pool.conditions?.length) {
      const cc = convConditions(pool.conditions);
      if (cc.length) out.conditions = cc;
    }
    out.entries = entries;
    pools.push(out);
  }
  if (!pools.length) { warn(`战利品表转换后为空: ${name}`); return { written: false }; }
  fs.writeFileSync(`${outDir}/${name}.json`, JSON.stringify({ type: 'minecraft:entity', pools }, null, 2) + '\n');
  if (dropped.length) log.info.push(`loot[${name}] 丢弃: ${[...new Set(dropped)].join(', ')}`);
  return { written: true };
}

// ============ 生成规则 → biome modifier ============
function convertSpawnRule(name) {
  const p = `${BP}/spawn_rules/${name}.json`;
  if (!fs.existsSync(p)) return null;
  const j = readJson(p);
  const sr = j['minecraft:spawn_rules'];
  const id = sr?.description?.identifier;
  if (id !== `dec:${name}`) { warn(`spawn_rules identifier 不匹配: ${name} (${id})`); return null; }
  const cond = (sr.conditions ?? [])[0];
  if (!cond) return null;
  const water = cond['minecraft:spawns_underwater'] !== undefined || cond['minecraft:spawns_on_medium_surface'] === undefined && cond['minecraft:spawns_underwater'] !== undefined;
  const onLand = cond['minecraft:spawns_on_surface'] !== undefined || cond['minecraft:spawns_underground'] !== undefined;
  const weight = cond['minecraft:weight']?.default ?? 8;
  const herd = cond['minecraft:herd'] ?? {};
  const minCount = Math.max(1, Math.min(4, herd.min_size ?? 1));
  const maxCount = Math.max(minCount, Math.min(4, herd.max_size ?? minCount));

  // 收集群系标签
  const tags = new Set();
  const scan = (o) => {
    if (Array.isArray(o)) { o.forEach(scan); return; }
    if (o && typeof o === 'object') {
      if (o.test === 'has_biome_tag' && typeof o.value === 'string') tags.add(o.value);
      for (const v of Object.values(o)) scan(v);
    }
  };
  scan(cond['minecraft:biome_filter']);
  let biomes = [];
  for (const t of tags) {
    const mapped = TAG_MAP[t];
    if (mapped) biomes.push(...mapped);
    else warn(`未知群系标签: ${t} (${name})`);
  }
  biomes = [...new Set(biomes)];
  if (!biomes.length) biomes = tags.size === 0 ? DEFAULT_BIOMES : (water ? OCEAN_BIOMES : DEFAULT_BIOMES);
  if (tags.size === 0 && water) biomes = OCEAN_BIOMES;

  // 写群系标签
  const tagDir = `${RES}/data/decisland/tags/worldgen/biome`;
  fs.mkdirSync(tagDir, { recursive: true });
  fs.writeFileSync(`${tagDir}/spawns_${name}.json`, JSON.stringify({ values: biomes }, null, 2) + '\n');
  const modDir = `${RES}/data/decisland/neoforge/biome_modifier`;
  fs.mkdirSync(modDir, { recursive: true });
  fs.writeFileSync(`${modDir}/${name}_spawns.json`, JSON.stringify({
    type: 'neoforge:add_spawns',
    biomes: `#decisland:spawns_${name}`,
    spawners: { type: `decisland:${name}`, weight, minCount, maxCount },
  }, null, 2) + '\n');
  return { water: !onLand && cond['minecraft:spawns_underwater'] !== undefined, weight, minCount, maxCount, biomes: biomes.length };
}

// ============ 几何/动画资源 ============
// 按几何体 identifier 精确查找（实体常引用其它名字的几何体，如 ash_sword_phantom → geometry.ash_sword）
let geoFileIndex = null;
function buildGeoFileIndex() {
  const idx = [];
  (function walk(d) {
    for (const f of fs.readdirSync(d)) {
      const p = `${d}/${f}`;
      const s = fs.statSync(p);
      if (s.isDirectory()) walk(p);
      else if (f.endsWith('.json')) {
        try {
          const j = readJson(p);
          const geos = j['minecraft:geometry'] ?? [];
          for (const g of (Array.isArray(geos) ? geos : [])) {
            const id = g?.description?.identifier;
            if (id) idx.push({ id, p, j });
          }
          // legacy 1.8.0 顶层键式几何体："geometry.xxx": { bones: [...] }
          for (const k of Object.keys(j)) {
            if (/^geometry\./.test(k) && j[k] && typeof j[k] === 'object' && !Array.isArray(j[k])) idx.push({ id: k, p, j });
          }
        } catch { /* skip */ }
      }
    }
  })(`${RP}/models`);
  return idx;
}
function loadGeometryFile(geometryId) {
  if (!geometryId) return null;
  if (!geoFileIndex) geoFileIndex = buildGeoFileIndex();
  return geoFileIndex.find((e) => e.id === geometryId) ?? null;
}

function extractGeometry(name, geometryId) {
  const found = loadGeometryFile(geometryId);
  if (!found) return null;
  const { p, j } = found;
  const outDir = `${RES}/assets/decisland/bedrock/models/entity`;
  fs.mkdirSync(outDir, { recursive: true });
  const outP = `${outDir}/${name}.geometry.json`;
  const geos = j['minecraft:geometry'];
  if (Array.isArray(geos) && geos.length > 0) {
    const match = geos.find((g) => g?.description?.identifier === geometryId) ?? geos[0];
    if (!match) return null;
    fs.writeFileSync(outP, JSON.stringify({ format_version: j.format_version ?? '1.12.0', 'minecraft:geometry': [match] }));
    return outP;
  }
  // legacy 1.8.0 顶层键式：仅保留目标几何体（加载器取文件内第一个 geometry.* 键）
  const legacyKeys = Object.keys(j).filter((k) => /^geometry\./.test(k) && j[k] && typeof j[k] === 'object' && !Array.isArray(j[k]));
  if (legacyKeys.length > 1) {
    const keep = legacyKeys.find((k) => k === geometryId) ?? legacyKeys[0];
    fs.writeFileSync(outP, JSON.stringify({ format_version: j.format_version ?? '1.8.0', [keep]: j[keep] }));
    return outP;
  }
  fs.copyFileSync(p, outP);
  return outP;
}

function extractAnimations(name) {
  if (!animIndex) animIndex = buildAnimIndex();
  const ce = parseClientEntity(name);
  const entries = Object.entries(ce.animations);
  const pick = (test) => {
    for (const [key, id] of entries) if (test(key) || test(id)) return id;
    return null;
  };
  const idleId = pick((k) => /idle|base_pose|default/i.test(k)) ?? pick((k) => !/walk|move|attack|look/i.test(k));
  const walkId = pick((k) => /walk|move|swim/i.test(k) && !/controller/i.test(k));
  const out = { format_version: '1.8.0', animations: {} };
  const addAnim = (id, outName) => {
    if (!id) return false;
    const hit = animIndex[id];
    // 基岩内置动画（humanoid.* / controller.animation.*）不在 RP 文件里，属正常缺失 → 仅 info
    if (!hit) {
      if (/^(animation\.humanoid\.|controller\.animation\.)/.test(id)) log.info.push(`内置动画未内联(忽略): ${id} (${name})`);
      else warn(`动画未找到: ${id} (${name})`);
      return false;
    }
    out.animations[outName] = hit.obj;
    return true;
  };
  const hasIdle = addAnim(idleId, 'idle');
  const hasWalk = addAnim(walkId ?? idleId, 'walk');
  const outDir = `${RES}/assets/decisland/bedrock/animations/entity`;
  fs.mkdirSync(outDir, { recursive: true });
  if (!out.animations.idle) out.animations.idle = { loop: true, animation_length: 0 };
  if (!out.animations.walk) out.animations.walk = { loop: true, animation_length: 0 };
  fs.writeFileSync(`${outDir}/${name}.animation.json`, JSON.stringify(out));
  return { hasIdle, hasWalk };
}

// ============ ModEntities 弹射物字段索引（远程攻击映射用） ============
function collectProjectileFields() {
  const t = fs.readFileSync(`${SRC}/entity/ModEntities.kt`, 'utf8');
  const map = {};
  for (const m of t.matchAll(/val ([A-Z0-9_]+): Supplier<EntityType<\w+>>/g)) {
    map[m[1].toLowerCase()] = m[1];
  }
  return map;
}

// ============ 主流程 ============
const ITEM_IDS = collectItemIds();
const PROJ_FIELDS = collectProjectileFields();
const PORT_LIST = buildPortList();
console.log(`移植清单: ${PORT_LIST.length} 个实体`);
log.info.push(`贴图ID池: dec ${[...ITEM_IDS].filter((i) => !i.startsWith('mc:')).length} / vanilla ${[...ITEM_IDS].filter((i) => i.startsWith('mc:')).length}`);

const results = [];
const texOverrideMap = {}; // name → 原版基类实体是否有自定义贴图（决定是否覆写 getTextureLocation）
const eggColors = {};     // name → { bg, hi } 刷怪蛋颜色（贴图平均色/名字哈希兜底）
const categorized = fs.existsSync('C:/Users/Administrator/AppData/Local/Temp/bedrock_entity_cats.txt')
  ? fs.readFileSync('C:/Users/Administrator/AppData/Local/Temp/bedrock_entity_cats.txt', 'utf8') : '';

for (const name of PORT_LIST) {
  const bh = parseBehavior(name);
  if (!bh) { warn(`行为文件缺失: ${name}`); continue; }
  const ce = parseClientEntity(name);
  const runtime = (() => {
    const j = readJson(`${BP}/entities/${name}.json`);
    return j['minecraft:entity'].description.runtime_identifier ?? null;
  })();

  let impl = null; // { kind: 'vanilla'|'custom', ... }
  const geoRuntime = matchVanillaGeo(ce.geometryId);
  if (runtime && VANILLA_MAP[runtime]) {
    impl = { kind: 'vanilla', ...VANILLA_MAP[runtime] };
  } else if (geoRuntime && VANILLA_MAP[geoRuntime]) {
    impl = { kind: 'vanilla', ...VANILLA_MAP[geoRuntime] };
  } else if (!ce.geometryId && !runtime) {
    // 无几何体也无 runtime → 回退原版人形（Zombie 模型 + AI）
    warn(`${name}: 无几何体/runtime → 回退原版 Zombie 人形`);
    impl = { kind: 'vanilla', ...VANILLA_MAP['minecraft:zombie'] };
  }
  if (!impl) {
    const geoP = ce.geometryId ? extractGeometry(name, ce.geometryId) : null;
    if (geoP) {
      const anims = extractAnimations(name);
      impl = { kind: 'custom', hasAnim: anims.hasIdle || anims.hasWalk };
    } else {
      warn(`无几何体资源: ${name} (runtime=${runtime}, geo=${ce.geometryId})`);
      continue;
    }
  }

  // 贴图
  let texOk = false;
  if (ce.texturePath) {
    let src = findTexture(ce.texturePath);
    // 基岩引用原版材质（textures/blocks/ice、textures/entity/skeleton/stray 等）→ 用 jar 提取的原版贴图
    if (!src) {
      const vp = `${PROJ}/tools/entity_port/vanilla_tex/${path.basename(ce.texturePath)}.png`;
      if (fs.existsSync(vp)) src = vp;
    }
    if (src) texOk = !!convertTexture(src, name, impl.kind === 'vanilla' && impl.human64 === true);
    else warn(`贴图未找到: ${ce.texturePath} (${name})`);
  } else if (impl.kind === 'vanilla') {
    texOk = true; // 无自定义贴图 → 不覆写 getTextureLocation，继承原版贴图
  }
  if (!texOk && impl.kind === 'custom') warn(`无贴图: ${name}`);
  texOverrideMap[name] = impl.kind === 'vanilla' && !!ce.texturePath && texOk;

  // 战利品 & 生成条件
  const loot = convertLoot(name);
  const spawn = convertSpawnRule(name);
  // 刷怪蛋颜色：优先贴图平均色
  eggColors[name] = eggColorsFor(name, `${RES}/assets/decisland/textures/entity/${name}.png`);

  results.push({ name, bh, ce, runtime, impl, loot, spawn });
}

// ============ Kotlin 代码生成 ============
const pascal = (s) => s.split('_').map((w) => w.charAt(0).toUpperCase() + w.slice(1)).join('');
const upper = (s) => s.toUpperCase();

// --- GeneratedVanillaEntities.kt ---
const usedImports = new Set(['net.minecraft.world.entity.EntityType', 'net.minecraft.world.level.Level']);
const entityClassNames = {};
let vtBody = '// 由 tools/entity_port/generate.mjs 生成：基岩版原版基类生物（原版 AI + 自定义贴图）\n';
for (const r of results.filter((x) => x.impl.kind === 'vanilla')) {
  const cn = pascal(r.name);
  let sup = r.impl.cls;
  const supFqn = `net.minecraft.world.entity.${r.impl.pkg}.${sup}`;
  if (cn === sup) {
    // 类名与原版基类同名：同文件声明遮蔽导入，需用全限定名引用父类（不导入）
    sup = supFqn;
  } else {
    usedImports.add(supFqn);
  }
  entityClassNames[r.name] = cn;
  let extra = '';
  if (r.impl.fire || r.bh.fireImmune) extra += '\n    override fun fireImmune() = true';
  vtBody += `\nclass ${cn}(entityType: EntityType<out ${sup}>, level: Level) : ${sup}(entityType, level) {${extra}\n}\n`;
}
const vt = `package com.dec.decisland.entity

import ${[...usedImports].sort().join('\nimport ')}

${vtBody}`;
fs.writeFileSync(`${SRC}/entity/GeneratedVanillaEntities.kt`, vt);

// --- GeneratedMobs.kt ---
let g = `package com.dec.decisland.entity

import com.dec.decisland.DecIsland
import com.dec.decisland.entity.custom.BedrockMob
import com.dec.decisland.entity.custom.MobConfig
import net.minecraft.core.registries.Registries
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.entity.SpawnPlacementTypes
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.monster.Monster
import net.minecraft.world.entity.Mob
import net.minecraft.world.item.Item
import net.minecraft.world.level.levelgen.Heightmap
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.common.DeferredSpawnEggItem
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

/** 由 tools/entity_port/generate.mjs 生成：基岩版生物批量移植注册表 */
object GeneratedMobs {
    @JvmField
    val ENTITY_TYPES: DeferredRegister<EntityType<*>> =
        DeferredRegister.create(Registries.ENTITY_TYPE, DecIsland.MOD_ID)

    @JvmField
    val SPAWN_EGGS: DeferredRegister.Items =
        DeferredRegister.createItems(DecIsland.MOD_ID)

    private fun <T : Entity> registerEntity(
        name: String,
        factory: (EntityType<T>, Level2) -> T,
        category: MobCategory,
        configure: EntityType.Builder<T>.() -> Unit = {},
    ): DeferredHolder<EntityType<*>, EntityType<T>> =
        ENTITY_TYPES.register(name, Supplier { EntityType.Builder.of(factory, category).apply(configure).build(name) })
`;
// Level2 占位替换为 Level（避免 import 冲突处理复杂）
g = g.replace(/Level2/g, 'Level').replace('import net.minecraft.world.entity.Entity\n', 'import net.minecraft.world.entity.Entity\nimport net.minecraft.world.entity.LivingEntity\nimport net.minecraft.world.level.Level\n');

// 客户端渲染器注册表数据（给 GeneratedMobClient 用）：写入 JSON sidecar，避免 Kotlin 里重复资源路径
const clientSpec = [];

const catOf = (r) => {
  const f = r.bh.families.join(',');
  if (r.bh.water && !r.bh.hostile) return 'WATER_CREATURE';
  if (r.bh.water && r.bh.hostile) return 'WATER_CREATURE';
  if (/villager|animal|peaceful/.test(f) && !r.bh.hostile) return 'CREATURE';
  if (r.impl.kind === 'vanilla' && ['Bat', 'Cod', 'Chicken', 'PolarBear', 'Wolf', 'IronGolem', 'SnowGolem'].includes(r.impl.cls)) {
    return (r.bh.hostile && !['IronGolem', 'SnowGolem', 'Wolf', 'PolarBear'].includes(r.impl.cls)) ? 'MONSTER' : 'CREATURE';
  }
  return 'MONSTER';
};

const attrEntries = [];
const spawnEntries = [];
const fqAttr = (r) => {
  const cls = `net.minecraft.world.entity.${r.impl.pkg}.${r.impl.cls}`;
  return ATTR_BUILDER[r.impl.cls] ?? `${cls}.createAttributes()`;
};
const ATTRS = 'net.minecraft.world.entity.ai.attributes.Attributes';
for (const r of results) {
  const cn = r.impl.kind === 'vanilla' ? entityClassNames[r.name] : 'BedrockMob';
  const cat = catOf(r);
  const w = Math.max(0.3, Math.min(6, +(r.bh.box.width ?? 0.6)));
  const h = Math.max(0.3, Math.min(8, +(r.bh.box.height ?? 1.8)));
  if (r.impl.kind === 'vanilla') {
    const fire = (r.impl.fire || r.bh.fireImmune) ? '.fireImmune()' : '';
    g += `
    @JvmField
    val ${upper(r.name)}: Supplier<EntityType<${cn}>> =
        registerEntity("${r.name}", { type, level -> ${cn}(type, level) }, MobCategory.${cat}) {
            sized(${w.toFixed(2)}f, ${h.toFixed(2)}f)${fire}
        }
`;
    // 属性：原版构建器 + 基岩血量/攻击差异覆盖
    let ov = '';
    const defHp = VANILLA_HP[r.impl.cls] ?? 20;
    if (Math.abs(r.bh.health - defHp) > 0.01) ov += `.add(${ATTRS}.MAX_HEALTH, ${Math.max(1, r.bh.health).toFixed(1)})`;
    if (r.impl.pkg === 'monster' && r.bh.melee) ov += `.add(${ATTRS}.ATTACK_DAMAGE, ${Math.max(0.5, r.bh.attack).toFixed(1)})`;
    attrEntries.push(`        ${upper(r.name)} to Supplier { ${fqAttr(r)}${ov} }`);
  } else {
    // custom BedrockMob
    const c = r.bh;
    const targets = c.targets.filter((t) => ['player', 'villager', 'iron_golem', 'snow_golem'].includes(t));
    const projField = (() => {
      const def = String(c.projectileDef ?? '').replace(/^dec:/, '').toLowerCase();
      return def && PROJ_FIELDS[def] ? `ModEntities.${PROJ_FIELDS[def]}` : null;
    })();
    const friendly = !c.hostile;
    const cfgArgs = [
      `"${r.name}"`,
      `health = ${Math.max(1, c.health).toFixed(1)}`,
      `speed = ${Math.max(0.08, c.movement > 0 ? c.movement : 0.25).toFixed(3)}`,
      `attackDamage = ${Math.max(0.5, c.attack).toFixed(1)}`,
      `followRange = ${Math.max(8, Math.min(96, c.follow)).toFixed(1)}`,
      `knockbackResistance = ${Math.max(0, Math.min(1, c.kb)).toFixed(2)}`,
      `xp = ${c.xp}`,
      `fireImmune = ${c.fireImmune || c.families.includes('fire_immune')}`,
      `undead = ${c.undead}`,
      `water = ${c.water}`,
      `friendly = ${friendly}`,
      `melee = ${c.melee}`,
      `rangedInterval = ${Math.max(8, Math.min(200, c.rangedInterval))}`,
      `targets = listOf(${targets.map((t) => `"${t}"`).join(', ') || '"player"'})`,
    ];
    if (projField) cfgArgs.push(`rangedProjectile = Supplier { ${projField}.get() }`);
    g += `
    private val CFG_${upper(r.name)} = MobConfig(${cfgArgs.join(', ')})

    @JvmField
    val ${upper(r.name)}: Supplier<EntityType<BedrockMob>> =
        registerEntity("${r.name}", { type, level -> BedrockMob(type, level, CFG_${upper(r.name)}) }, MobCategory.${cat}) {
            sized(${w.toFixed(2)}f, ${h.toFixed(2)}f)
        }
`;
    attrEntries.push(`        ${upper(r.name)} to Supplier { BedrockMob.createAttributes(CFG_${upper(r.name)}) }`);
  }
  // 刷怪蛋：颜色取贴图平均色（兜底名字哈希）
  const ec = eggColors[r.name];
  g += `
    @JvmField
    val ${upper(r.name)}_EGG: DeferredItem<DeferredSpawnEggItem> =
        SPAWN_EGGS.register("${r.name}_spawn_egg", Supplier {
            DeferredSpawnEggItem(Supplier { ${upper(r.name)}.get() }, ${ec.bg}, ${ec.hi}, Item.Properties())
        })
`;
  if (r.spawn) {
    const rule = r.bh.water || r.spawn.water ? 'WATER' : (cat === 'MONSTER' ? 'MONSTER' : 'MOB');
    spawnEntries.push(`        SpawnEntry(${upper(r.name)}, "${rule}")`);
  }
  clientSpec.push({
    name: r.name, kind: r.impl.kind, sup: r.impl.kind === 'vanilla' ? r.impl.cls : null,
    ren: r.impl.kind === 'vanilla' ? r.impl.ren : null, renParam: r.impl.kind === 'vanilla' ? r.impl.rp : null,
    renPkg: r.impl.kind === 'vanilla' ? r.impl.pkgR : null,
    renGeneric: r.impl.kind === 'vanilla' ? (GENERIC_REN_PARAM[r.impl.ren] ?? null) : null,
    scale: +(r.bh.scale ?? 1).toFixed(2), texOverride: r.impl.kind === 'vanilla' && texOverrideMap[r.name] === true,
  });
}

// ==== entity_type 标签（1.21.1 数据驱动：水生呼吸 / 亡灵分类） ====
const waterTagIds = [];
const undeadTagIds = [];
for (const r of results) {
  const cat = catOf(r);
  const isWater = r.bh.water || cat === 'WATER_CREATURE' || (r.spawn && r.spawn.water);
  if (isWater) waterTagIds.push(`decisland:${r.name}`);
  const vanillaUndead = r.impl.kind === 'vanilla' && ['Zombie', 'Skeleton', 'WitherSkeleton', 'Phantom'].includes(r.impl.cls);
  if (r.bh.undead || vanillaUndead) undeadTagIds.push(`decisland:${r.name}`);
}
const etTagDir = `${RES}/data/minecraft/tags/entity_type`;
fs.mkdirSync(etTagDir, { recursive: true });
if (waterTagIds.length) {
  fs.writeFileSync(`${etTagDir}/can_breathe_under_water.json`, JSON.stringify({ values: waterTagIds }, null, 2) + '\n');
  console.log(`entity_type 标签: can_breathe_under_water=${waterTagIds.length}`);
}
if (undeadTagIds.length) {
  for (const t of ['undead', 'ignores_poison_and_regen', 'inverted_healing_and_harm', 'wither_friends', 'sensitive_to_smite']) {
    fs.writeFileSync(`${etTagDir}/${t}.json`, JSON.stringify({ values: undeadTagIds }, null, 2) + '\n');
  }
  console.log(`entity_type 标签: undead×5=${undeadTagIds.length}`);
}

// ==== 刷怪蛋物品模型（template_spawn_egg 纯色蛋，颜色由物品注册时的 int 决定） ====
const eggModelDir = `${RES}/assets/decisland/models/item`;
fs.mkdirSync(eggModelDir, { recursive: true });
for (const r of results) {
  fs.writeFileSync(`${eggModelDir}/${r.name}_spawn_egg.json`, `${JSON.stringify({ parent: 'minecraft:item/template_spawn_egg' }, null, 2)}\n`);
}
console.log(`刷怪蛋: ${results.length} 个（物品模型 + 注册）`);

g += `
    // ==== 属性 ====
    data class SpawnEntry(val type: Supplier<out EntityType<*>>, val rule: String)

    @JvmStatic
    val ATTRIBUTE_ENTRIES: List<Pair<Supplier<out EntityType<*>>, Supplier<AttributeSupplier.Builder>>> = listOf(
${attrEntries.join(',\n')}
    )

    @JvmStatic
    val SPAWN_ENTRIES: List<SpawnEntry> = listOf(
${spawnEntries.join(',\n')}
    )

    @JvmStatic
    fun register(modEventBus: IEventBus) {
        ENTITY_TYPES.register(modEventBus)
        SPAWN_EGGS.register(modEventBus)
    }

    @JvmStatic
    fun registerAttributes(event: EntityAttributeCreationEvent) {
        for ((type, builder) in ATTRIBUTE_ENTRIES) {
            // Builder 必须在事件回调期构建：<clinit> 时 NeoForge 扩展属性（swim_speed 等）尚未绑定会 NPE
            event.put(@Suppress("UNCHECKED_CAST") (type.get() as EntityType<out LivingEntity>), builder.get().build())
        }
    }

    @JvmStatic
    fun registerSpawnPlacements(event: RegisterSpawnPlacementsEvent) {
        for ((type, rule) in SPAWN_ENTRIES) {
            if (rule == "MONSTER") {
                val t = @Suppress("UNCHECKED_CAST") (type.get() as EntityType<Monster>)
                event.register(t, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE)
            } else {
                val t = @Suppress("UNCHECKED_CAST") (type.get() as EntityType<Mob>)
                val placement = if (rule == "WATER") SpawnPlacementTypes.IN_WATER else SpawnPlacementTypes.ON_GROUND
                event.register(t, placement, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE)
            }
        }
    }
}
`;
fs.writeFileSync(`${SRC}/entity/GeneratedMobs.kt`, g);
fs.writeFileSync('f:/DecIslandNeoforge-main/tools/entity_port/client_spec.json', JSON.stringify(clientSpec, null, 1));

// --- 客户端渲染器 GeneratedMobClient.kt ---
const vanillaRens = clientSpec.filter((c) => c.kind === 'vanilla');
const customRens = clientSpec.filter((c) => c.kind === 'custom');
let rc = `package com.dec.decisland.client.renderer

import com.dec.decisland.DecIsland
import com.dec.decisland.entity.GeneratedMobs
import com.dec.decisland.entity.custom.BedrockMob
import net.minecraft.client.renderer.entity.EntityRenderers
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.resources.ResourceLocation
`;
const renImports = new Set();
for (const c of vanillaRens) {
  renImports.add(`net.minecraft.client.renderer.entity.${c.ren}`); // 渲染器均在根包（已核验）
  renImports.add(`net.minecraft.world.entity.${c.renPkg}.${c.renParam}`);
  if (c.sup !== c.renParam) renImports.add(`net.minecraft.world.entity.${c.renPkg}.${c.sup}`);
}
rc += `import ${[...renImports].sort().join('\nimport ')}\n\n`;

rc += `/** 由 tools/entity_port/generate.mjs 生成：批量实体渲染器（原版模型换贴图 + 基岩模型通用渲染） */\n`;
const renClassNames = {};
for (const c of vanillaRens) {
  const cn = `${pascal(c.name)}GeneratedRenderer`;
  renClassNames[c.name] = cn;
  const generic = c.renGeneric ? `<${c.renGeneric}>` : '';
  // texOverride=false → 不覆写 getTextureLocation，继承原版贴图
  const override = c.texOverride
    ? `\n    override fun getTextureLocation(entity: ${c.renParam}): ResourceLocation = TEXTURE`
    : '';
  const companion = c.texOverride
    ? `\n    companion object {\n        val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/${c.name}.png")\n    }\n`
    : '\n';
  rc += `class ${cn}(context: EntityRendererProvider.Context) : ${c.ren}${generic}(context) {${override}${companion}}\n`;
}

rc += `object GeneratedMobClient {
    @JvmStatic
    fun registerAll() {
`;
for (const c of vanillaRens) {
  rc += `        EntityRenderers.register<${c.renGeneric ?? c.sup}>(GeneratedMobs.${upper(c.name)}.get(), ::${renClassNames[c.name]})\n`;
}
for (const c of customRens) {
  rc += `        EntityRenderers.register(GeneratedMobs.${upper(c.name)}.get()) { context ->
            BedrockMobRenderer<BedrockMob>(context, geo("${c.name}"), anim("${c.name}"), "idle", "walk", tex("${c.name}"), ${c.scale}f)
        }
`;
}
rc += `    }

    private fun geo(name: String) = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "bedrock/models/entity/$name.geometry.json")
    private fun anim(name: String) = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "bedrock/animations/entity/$name.animation.json")
    private fun tex(name: String) = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/entity/$name.png")
}
`;
fs.writeFileSync(`${SRC}/client/renderer/GeneratedMobClient.kt`, rc);

// ============ 汇总 ============
console.log(`\n=== 完成 ${results.length} 个实体 ===`);
console.log(`原版基类: ${results.filter((r) => r.impl.kind === 'vanilla').length}, 自定义模型: ${results.filter((r) => r.impl.kind === 'custom').length}`);
console.log(`战利品表: ${results.filter((r) => r.loot.written).length}, 生成条件: ${results.filter((r) => r.spawn).length}`);
fs.writeFileSync('f:/DecIslandNeoforge-main/tools/entity_port/gen_report.txt',
  `WARN (${log.warn.length}):\n${log.warn.map((w) => '  ' + w).join('\n')}\n\nINFO:\n${log.info.map((w) => '  ' + w).join('\n')}\n`);
console.log(`警告 ${log.warn.length} 条 → tools/entity_port/gen_report.txt`);
