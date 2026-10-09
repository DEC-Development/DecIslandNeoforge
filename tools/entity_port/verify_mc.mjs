// 验证 mc_sources 中渲染器/实体类签名 + 完整物品表 + 原版贴图可用性
import fs from 'fs';

const MC = 'f:/DecIslandNeoforge-main/mc_sources';
const out = {};

// 1) 渲染器类声明（是否泛型）+ getTextureLocation 签名
const renderers = ['ZombieRenderer','DrownedRenderer','SkeletonRenderer','StrayRenderer','WitherSkeletonRenderer',
  'CreeperRenderer','BatRenderer','GhastRenderer','BlazeRenderer','SlimeRenderer','MagmaCubeRenderer','VexRenderer',
  'GuardianRenderer','IronGolemRenderer','SnowGolemRenderer','PolarBearRenderer','CaveSpiderRenderer','SpiderRenderer',
  'EndermanRenderer','EnderManRenderer','CodRenderer','ChickenRenderer','EvokerRenderer','SilverfishRenderer',
  'WolfRenderer','PillagerRenderer','VindicatorRenderer','PhantomRenderer','CowRenderer','VillagerRenderer',
  'WitchRenderer','SalmonRenderer','IllusionerRenderer'];
out.renderers = {};
function findFile(name) {
  const hits = [];
  (function walk(d) {
    if (!fs.existsSync(d)) return;
    for (const f of fs.readdirSync(d)) {
      const p = `${d}/${f}`;
      if (fs.statSync(p).isDirectory()) walk(p);
      else if (f === `${name}.java`) hits.push(p);
    }
  })(`${MC}/net/minecraft/client/renderer/entity`);
  return hits;
}
for (const r of renderers) {
  const hits = findFile(r);
  if (!hits.length) { out.renderers[r] = null; continue; }
  const t = fs.readFileSync(hits[0], 'utf8');
  const decl = t.match(/public class \w+(<[^>]+>)?\s+extends\s+([^\s{]+)/);
  out.renderers[r] = {
    file: hits[0].replace(MC + '/', ''),
    decl: decl ? decl[0] : t.split('\n').find((l) => l.includes('class '))?.trim(),
    generic: /public class \w+</.test(t),
  };
}

// 2) 实体类 createAttributes / 构造器
const entities = ['Zombie','Drowned','Skeleton','Stray','WitherSkeleton','Creeper','Bat','Ghast','Blaze','Slime',
  'MagmaCube','Vex','Guardian','IronGolem','SnowGolem','PolarBear','CaveSpider','Spider','EnderMan','Cod','Chicken',
  'Evoker','Silverfish','Wolf','Pillager','Vindicator','Phantom','Cow','Villager','Witch','Salmon','Illusioner','SpellcasterIllager','AbstractSkeleton'];
out.entities = {};
for (const e of entities) {
  const hits = [];
  (function walk(d) {
    for (const f of fs.readdirSync(d)) {
      const p = `${d}/${f}`;
      if (fs.statSync(p).isDirectory()) walk(p);
      else if (f === `${e}.java`) hits.push(p);
    }
  })(`${MC}/net/minecraft/world/entity`);
  if (!hits.length) { out.entities[e] = null; continue; }
  const t = fs.readFileSync(hits[0], 'utf8');
  out.entities[e] = {
    pkg: hits[0].split('world/entity/')[1].replace(/\/[^/]+$/, ''),
    decl: t.match(/public class \w+(<[^>]+>)?\s+extends\s+([^\s<{]+)/)?.[0] ?? t.split('\n').find((l) => l.includes('class '))?.trim(),
    createAttributes: /static AttributeSupplier\.Builder createAttributes\w*\(/.test(t),
    ctor: t.match(/public \w+\(EntityType<[^)]*\)/)?.[0] ?? null,
  };
}

// 3) Items.java 完整物品注册名
const items = new Set();
const itemsT = fs.readFileSync(`${MC}/net/minecraft/world/item/Items.java`, 'utf8');
for (const m of itemsT.matchAll(/register(?:Item|Block|)\("([a-z0-9_.]+)"/g)) items.add(m[1]);
for (const m of itemsT.matchAll(/=\s*register[A-Za-z]*\("([a-z0-9_.]+)"/g)) items.add(m[1]);
out.vanillaItemCount = items.size;
out.hasItems = ['netherrack','snow','ice','tnt','sponge','music_disc_13','cod','skeleton_skull','lily_pad',
  'nether_quartz_ore','poppy','sweet_berries','amethyst_shard','copper_ingot','iron_sword','bow','crossbow',
  'enchanted_book','iron_helmet','netherite_scrap','crimson_fungus','nether_wart_block','end_stone','coal_ore',
  'diamond_ore','redstone','end_pearl'].map((i) => [i, items.has(i)]);
fs.writeFileSync('f:/DecIslandNeoforge-main/tools/entity_port/vanilla_items.json', JSON.stringify([...items], null, 0));

// 4) 原版贴图（供 fallback）
out.vanillaTex = {};
for (const rel of ['block/ice.png','entity/skeleton/stray.png','entity/zombie/zombie.png','block/tnt.png']) {
  out.vanillaTex[rel] = fs.existsSync(`${MC}/assets/minecraft/textures/${rel}`);
}

fs.writeFileSync('f:/DecIslandNeoforge-main/tools/entity_port/verify_mc.json', JSON.stringify(out, null, 1));
console.log('renderers:', Object.entries(out.renderers).map(([k, v]) => `${k}=${v ? (v.generic ? 'GENERIC' : 'plain') : 'MISSING'}`).join('\n  '));
console.log('entities:', Object.entries(out.entities).map(([k, v]) => `${k}=${v ? `${v.pkg} attrs=${v.createAttributes}` : 'MISSING'}`).join('\n  '));
console.log('vanillaItemCount:', out.vanillaItemCount);
console.log('hasItems:', JSON.stringify(out.hasItems));
console.log('vanillaTex:', JSON.stringify(out.vanillaTex));
