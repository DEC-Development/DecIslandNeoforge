import json
for n in ['ghost_sword','growth','radiate_sword','scale_sword','warden_sword','wind_of_shadow','storm_giant_sword','bamboo_yataghan']:
    d = json.load(open(r'_tmp_bedrock_source/DecIslandB/ex_items/%s.json'%n, encoding='utf-8'))
    it = d['minecraft:item']
    c = it.get('components', {})
    print('===== ' + n)
    for k in ['minecraft:chargeable','minecraft:use_modifiers','minecraft:throwable','minecraft:fuel','minecraft:digger']:
        if k in c: print(k + ': ' + json.dumps(c[k], ensure_ascii=False))
    print('EVENTS: ' + json.dumps(it.get('events',{}), ensure_ascii=False))
    print()
