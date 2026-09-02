import json
swords = ['bamboo_yataghan','blue_of_the_sea','decrepit_atlantis','dust_destroyer','everlasting_winter_sword','ghost_sword','growth','radiate_sword','scale_sword','star_sword','storm_giant_sword','sword_of_deep','sword_of_guard','sword_of_halloween','the_imperial_sword','village_guardian','vortex','warden_sword','wind_of_shadow']
out = []
for n in swords:
    d = json.load(open(r'_tmp_bedrock_source/DecIslandB/ex_items/%s.json'%n, encoding='utf-8'))
    it = d['minecraft:item']
    c = it.get('components', {})
    ev = it.get('events', {})
    out.append('===== ' + n)
    out.append('durability: ' + str(c.get('minecraft:durability',{}).get('max_durability')))
    out.append('damage: ' + str(c.get('minecraft:damage')))
    out.append('enchant: ' + str(c.get('minecraft:enchantable')))
    out.append('cooldown: ' + str(c.get('minecraft:cooldown')))
    out.append('repair: ' + json.dumps(c.get('minecraft:repairable'), ensure_ascii=False))
    out.append('weapon: ' + json.dumps(c.get('minecraft:weapon'), ensure_ascii=False))
    out.append('on_use: ' + json.dumps(c.get('minecraft:on_use'), ensure_ascii=False))
    out.append('other components: ' + ', '.join(k for k in c if k not in ('minecraft:display_name','minecraft:max_stack_size','minecraft:hand_equipped','minecraft:icon','minecraft:durability','minecraft:damage','minecraft:enchantable','minecraft:cooldown','minecraft:repairable','minecraft:weapon','minecraft:on_use','minecraft:stacked_by_data','minecraft:mining_speed','minecraft:can_destroy_in_creative','minecraft:digger','category','trade_reference','minecraft:tags')))
    out.append('EVENTS: ' + json.dumps(ev, ensure_ascii=False))
open('_tmp_swords_dump.txt','w',encoding='utf-8').write('\n'.join(out))
print('done')
