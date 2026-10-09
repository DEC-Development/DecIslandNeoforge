// 补充验证：属性方法名 / Blocks.java 物品 / references Items.java / 客户端 jar
import fs from 'fs';

const MC = 'f:/DecIslandNeoforge-main/mc_sources';
const out = {};

// 属性 builder 方法（宽匹配）
for (const [n, p] of Object.entries({
  Slime: 'monster/Slime.java', Phantom: 'monster/Phantom.java', Cod: 'animal/Cod.java',
  Salmon: 'animal/Salmon.java', AbstractFish: 'animal/AbstractFish.java', AbstractSkeleton: 'monster/AbstractSkeleton.java',
  Spider: 'monster/Spider.java', SnowGolem: 'animal/SnowGolem.java', Bat: 'ambient/Bat.java',
})) {
  const t = fs.readFileSync(`${MC}/net/minecraft/world/entity/${p}`, 'utf8');
  out[n] = (t.match(/static AttributeSupplier\.Builder (\w+)\(/g) ?? []).map((s) => s.replace('static AttributeSupplier.Builder ', ''));
}

// Items.java 两个文件对比
for (const f of ['net/minecraft/world/item/Items.java', 'net/minecraft/references/Items.java']) {
  const p = `${MC}/${f}`;
  if (!fs.existsSync(p)) { out[f] = 'missing'; continue; }
  const t = fs.readFileSync(p, 'utf8');
  const names = new Set();
  for (const m of t.matchAll(/register[A-Za-z]*\("([a-z0-9_.]+)"/g)) names.add(m[1]);
  out[f] = { size: t.length, items: names.size, sample: [...names].slice(0, 5) };
}

// Blocks.java register 字符串
const bt = fs.readFileSync(`${MC}/net/minecraft/world/level/block/Blocks.java`, 'utf8');
const blocks = new Set();
for (const m of bt.matchAll(/register[A-Za-z]*\("([a-z0-9_.]+)"/g)) blocks.add(m[1]);
out.blocksCount = blocks.size;
out.blocksSample = ['netherrack', 'snow', 'ice', 'tnt', 'sponge', 'lily_pad', 'nether_quartz_ore', 'poppy', 'end_stone', 'coal_ore', 'water', 'lava'].map((b) => [b, blocks.has(b)]);

// 客户端 jar 探测
const vDir = 'E:/.minecraft/versions/1.21.1-NeoForge_21.1.247';
out.jars = fs.existsSync(vDir) ? fs.readdirSync(vDir).filter((f) => f.endsWith('.jar')).map((f) => `${f} ${Math.round(fs.statSync(`${vDir}/${f}`).size / 1e6)}MB`) : [];

fs.writeFileSync('f:/DecIslandNeoforge-main/tools/entity_port/verify_mc2.json', JSON.stringify(out, null, 1));
console.log(JSON.stringify(out, null, 1));
