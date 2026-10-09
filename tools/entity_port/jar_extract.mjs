// 从客户端 jar 提取：物品清单（models/item/*）+ 指定贴图。纯 Node 解析 ZIP。
import fs from 'fs';
import zlib from 'zlib';

const JAR = 'E:/.minecraft/versions/1.21.1-NeoForge_21.1.247/1.21.1-NeoForge_21.1.247.jar';
const buf = fs.readFileSync(JAR);

// 定位 EOCD
let eocd = -1;
for (let i = buf.length - 22; i >= Math.max(0, buf.length - 65558); i--) {
  if (buf.readUInt32LE(i) === 0x06054b50) { eocd = i; break; }
}
if (eocd < 0) throw new Error('EOCD not found');
const count = buf.readUInt16LE(eocd + 10);
let off = buf.readUInt32LE(eocd + 16);

const entries = [];
for (let i = 0; i < count; i++) {
  if (buf.readUInt32LE(off) !== 0x02014b50) break;
  const method = buf.readUInt16LE(off + 10);
  const compSize = buf.readUInt32LE(off + 20);
  const nameLen = buf.readUInt16LE(off + 28);
  const extraLen = buf.readUInt16LE(off + 30);
  const commentLen = buf.readUInt16LE(off + 32);
  const lho = buf.readUInt32LE(off + 42);
  const name = buf.toString('utf8', off + 46, off + 46 + nameLen);
  entries.push({ name, method, compSize, lho });
  off += 46 + nameLen + extraLen + commentLen;
}
console.error(`jar 条目: ${entries.length}`);

function extract(name) {
  const e = entries.find((x) => x.name === name);
  if (!e) return null;
  const lh = e.lho;
  const nameLen = buf.readUInt16LE(lh + 26);
  const extraLen = buf.readUInt16LE(lh + 28);
  const dataStart = lh + 30 + nameLen + extraLen;
  const raw = buf.subarray(dataStart, dataStart + e.compSize);
  return e.method === 0 ? Buffer.from(raw) : zlib.inflateRawSync(raw);
}

// 1) 物品清单
const items = entries.filter((e) => /^assets\/minecraft\/models\/item\/[a-z0-9_.]+\.json$/.test(e.name))
  .map((e) => e.name.slice('assets/minecraft/models/item/'.length, -5));
fs.writeFileSync('f:/DecIslandNeoforge-main/tools/entity_port/vanilla_items.json', JSON.stringify(items.sort(), null, 0));
console.log(`物品数: ${items.length}`);
const check = ['netherrack', 'snow', 'ice', 'tnt', 'sponge', 'music_disc_13', 'cod', 'skeleton_skull', 'lily_pad',
  'nether_quartz_ore', 'poppy', 'sweet_berries', 'amethyst_shard', 'copper_ingot', 'iron_sword', 'bow', 'crossbow',
  'enchanted_book', 'iron_helmet', 'netherite_scrap', 'crimson_fungus', 'nether_wart_block', 'end_stone', 'coal_ore', 'redstone'];
console.log('抽检:', check.map((c) => `${c}=${items.includes(c)}`).join(' '));

// 2) 提取贴图（覆盖冰刺贴图等基岩引用原版材质的情况）
const need = {
  'assets/minecraft/textures/block/ice.png': 'f:/DecIslandNeoforge-main/tools/entity_port/vanilla_tex/ice.png',
  'assets/minecraft/textures/block/tnt.png': 'f:/DecIslandNeoforge-main/tools/entity_port/vanilla_tex/tnt.png',
  'assets/minecraft/textures/block/blackstone.png': 'f:/DecIslandNeoforge-main/tools/entity_port/vanilla_tex/blackstone.png',
  'assets/minecraft/textures/entity/skeleton/stray.png': 'f:/DecIslandNeoforge-main/tools/entity_port/vanilla_tex/stray.png',
};
fs.mkdirSync('f:/DecIslandNeoforge-main/tools/entity_port/vanilla_tex', { recursive: true });
for (const [src, dst] of Object.entries(need)) {
  const d = extract(src);
  if (d) { fs.writeFileSync(dst, d); console.log(`提取 ${src} → ${dst} (${d.length}B)`); }
  else console.log(`缺失 ${src}`);
}
