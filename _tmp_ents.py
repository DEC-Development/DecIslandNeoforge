import json, glob
for name in ['bamboo_yataghan','hard_bubble','storm_energy','spots_by_sword','radiate_fog','pumpkin_bomb','wither_cloud','village_portal']:
    for f in sorted(glob.glob(r'_tmp_bedrock_source/DecIslandB/entities/%s*.json' % name)):
        d = json.load(open(f, encoding='utf-8'))
        ent = d.get('minecraft:entity', {})
        c = ent.get('components', {})
        print('===== ' + f.split('\\\\')[-1])
        keep = {}
        for k, v in c.items():
            if any(s in k for s in ['projectile','collision','health','physics','on_hit','damage','environment','timer','explode','spell','behavior']):
                keep[k] = v
        print(json.dumps(keep, ensure_ascii=False)[:1500])
        ev = ent.get('events', {})
        if ev: print('events: ' + json.dumps(ev, ensure_ascii=False)[:600])
        print()
