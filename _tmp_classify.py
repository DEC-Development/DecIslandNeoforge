import json, glob
missing = [l.strip() for l in open(r'_tmp_missing.txt') if l.strip()]
names = {}
for f in [r'_tmp_bedrock_source/DecIslandR/texts/en_US.lang', r'_tmp_bedrock_source/DecIslandR/texts/zh_CN.lang']:
    lang = 'en' if 'en_US' in f else 'zh'
    for line in open(f, encoding='utf-8', errors='replace'):
        if '=' in line and line.startswith('item.dec:'):
            k, v = line.strip().split('=', 1)
            key = k.split(':',1)[1].replace('.name','')
            names.setdefault(key, {})[lang] = v
cats = {}
for f in glob.glob(r'_tmp_bedrock_source/DecIslandB/ex_items/*.json'):
    try:
        d = json.load(open(f, encoding='utf-8'))
        it = d.get('minecraft:item', {})
        ident = it.get('description', {}).get('identifier','').replace('dec:','')
        grp = it.get('description', {}).get('menu_category', {}).get('group', '?')
        cats[ident] = grp
    except Exception:
        pass
out = []
for m in missing:
    nm = names.get(m, {})
    grp = str(cats.get(m, '?')).replace('minecraft:itemGroup.name.','').replace('dec:itemGroup.name.','dec:')
    out.append(m + ' | ' + nm.get('en','-') + ' | ' + nm.get('zh','-') + ' | ' + grp)
open('_tmp_classified.txt','w',encoding='utf-8').write('\n'.join(out))
print('written', len(out))
