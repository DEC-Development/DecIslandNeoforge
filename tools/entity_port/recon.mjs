// 勘察脚本：主类注册调用 + 物品ID清单
import fs from 'fs';
import path from 'path';

const main = fs.readFileSync('f:/DecIslandNeoforge-main/src/main/java/com/dec/decisland/DecIsland.kt', 'utf8');
console.log('=== DecIsland.kt register calls ===');
console.log(main.split('\n').filter(l => l.includes('.register(')).map(l => l.trim()).join('\n'));

const files = [];
(function walk(d) {
  for (const f of fs.readdirSync(d)) {
    const p = path.join(d, f);
    if (fs.statSync(p).isDirectory()) walk(p);
    else if (f.endsWith('.kt')) files.push(p);
  }
})('f:/DecIslandNeoforge-main/src/main/java/com/dec/decisland/item');
const itemSrc = files.map(f => fs.readFileSync(f, 'utf8')).join('\n');
// 常见注册模式: registerItem("name", ...) / "name" to Supplier
const ids = new Set();
for (const m of itemSrc.matchAll(/register(?:Item)?\(\s*"([a-z0-9_]+)"/g)) ids.add(m[1]);
console.log('=== register() style ids:', ids.size, '===');
console.log([...ids].slice(0, 20).join(', '));
// 找到 ModItems 文件里第一段注册示例
const modItemsPath = files.find(f => f.endsWith('ModItems.kt'));
if (modItemsPath) {
  const head = fs.readFileSync(modItemsPath, 'utf8').split('\n').filter(l => l.trim().length && !l.trim().startsWith('import')).slice(0, 40);
  console.log('=== ModItems head ===');
  console.log(head.join('\n'));
}
