# DecIsland Neoforge Mod

A mod developed by DecTeam.

## License

This mod is licensed under the Mozilla license.

## Installation

Please wait for our development. When we finish most of the contents, we will release the mod. You could give some advice to us.

## Contact

BiliBili: LiLeyi

## Version

So far, we support Minecraft 1.21.11 and Neoforge 21.11.38-beta

## 物品移植进度（基岩版 → JE）

对比基准：`_tmp_bedrock_source/DecIslandB/ex_items/`（基岩 536 个）vs `list/items.txt`（JE 注册表，由 runData 生成）。
最后更新：2026-09-02。剩余 **69** 个未移植。

注意：双格作物（方块）暂缓；copper/iron_bow 盔甲为基岩废弃内容，不移植。

### 剑 ×19
- [ ] bamboo_yataghan 竹长剑
- [ ] blue_of_the_sea 海之忧伤
- [x] decrepit_atlantis 破旧的亚特兰蒂斯
- [x] dust_destroyer 尘灭者
- [ ] everlasting_winter_sword 永冬长剑
- [x] ghost_sword 灵魂吸收者
- [x] growth 生长者
- [ ] radiate_sword 堕落圣刃
- [ ] scale_sword 鳞之刃
- [ ] star_sword 迸发之星
- [ ] storm_giant_sword 风暴巨剑
- [ ] sword_of_deep 黑域
- [x] sword_of_guard 侍卫之灵
- [x] sword_of_halloween 万圣之刃
- [ ] the_imperial_sword 尚方宝剑
- [ ] village_guardian 村庄守卫
- [ ] vortex 旋涡
- [ ] warden_sword 匿潜刃
- [ ] wind_of_shadow 暗影之风

### 法杖 ×10
- [ ] bat_staff 蝙蝠杖
- [ ] blaze_staff 烈焰法杖
- [ ] chaos_staff 混沌召唤杖
- [ ] chicken_staff 小鸡法杖
- [ ] dust_staff 尘埃召唤杖
- [ ] ghost_summoner 唤魂者
- [ ] god_of_sun 太阳神
- [ ] natural_spear 自然之矛
- [ ] radiate_spreader 堕落之神
- [ ] spider_staff 蜘蛛法杖

### 破坏神杖 ×2
- [ ] destroy_staff 破坏神杖
- [ ] god_of_destroy 破坏之神

### 图腾 ×4
- [ ] energy_totem 能量图腾
- [ ] fire_totem 火焰图腾
- [ ] gingerbread_totem 姜饼图腾
- [ ] ocean_totem 海洋图腾

### 枪械 ×3 + 弹药 ×2
- [ ] bomber 轰炸者
- [ ] catapult 弹弓
- [ ] echo_of_deep 深渊回影
- [ ] bomber_bullet 轰炸弹
- [ ] exploding_pellets 爆炸弹丸

### 投掷杂项 ×6
- [ ] dragon_fireball_by_player 龙息弹
- [ ] grapeshot_frozen_ball 霰弹冰霜魔法球
- [ ] leaves_knife 绿叶飞刀
- [ ] levitation_cloud 悬浮云
- [ ] poison_bag 毒囊
- [ ] soul_fireball 灵魂烈焰弹

### 导航/载具 ×10
- [ ] fire_heart 烈火之心
- [ ] ghost_dirt_wall 鬼魂土墙
- [ ] hook_rope 钩锁
- [ ] ice_hook_rope 极寒钩锁
- [ ] levitation_dirt_wall 飘浮土墙
- [ ] magic_scroll_blue 蓝魔法卷轴
- [ ] paraffin_bucket 煤油桶
- [ ] scarecrow 稻草人
- [ ] simple_car 简单小车（骑乘实体，工程量大）
- [ ] simple_glider 简单滑翔机（骑乘实体，工程量大）

### 装饰 ×8
- [ ] christmas_gift 圣诞礼物
- [ ] christmas_sock 圣诞袜
- [ ] drift_bottle 漂流瓶
- [ ] golden_fence 流金栅栏
- [ ] magic_mask_bag 魔法面具袋
- [ ] patterned_vase_red 红色纹饰花瓶
- [ ] red_bag 红包
- [ ] unknow_book 未知的书

### 零散 ×7
- [ ] angel_purification 净化使者（矛）
- [ ] wooden_enchant_book 木制附魔书（魔法书）
- [ ] tear_from_dream 梦境之泪（魔法饰品）
- [ ] blood_meat 血腥肉块（夜间活动物品）
- [ ] everlasting_winter_heart 永冬之心（夜间活动物品）

## 剑 ×19 移植方案（2026-09-02 侦查完毕）

### 约定
- 面板伤害 = 1 + 材质加成 + 参数，对齐基岩 `minecraft:damage`；攻速统一 -2.4（JE 剑标准）
- 技能冷却用 `useCooldown`；`magicpoint` 走 `ManaManager`；基岩事件自伤魔法 1 = 命中扣 1 耐久（WEAPON 组件）
- **材质严格专属**：每把剑单独建 `ModToolMaterial`，不复用已有材质（不可损坏的剑不给耐久）
- **粒子不将就**：未移植的基岩粒子必须先移植，不用现有粒子顶替
- 村庄守卫做**占位版**：被动（命中给附近村民回血）完整实现；技能（召唤村庄传送门）因 boss/传送门未移植，只做前置检查与提示，不生成传送门，后续补齐

### A 批：简单/被动
| 剑 | 机制 | 要点 |
|---|---|---|
| [x] decrepit_atlantis 破旧的亚特兰蒂斯 | 无技能，不可损坏 | 纯属性，无耐久，伤害 6，附魔 15 |
| [x] sword_of_guard 侍卫之灵 | 右键耗魔 1 → 自身抗性 5s | 耐久 231 / 伤害 6 / 附魔 15，铁修复 |
| [x] sword_of_halloween 万圣之刃 | 命中 1/5 概率召唤南瓜炸弹 | 炸弹：3s 引信、威力 2、带火；耐久 513 / 伤害 5 |
| [x] ghost_sword 灵魂吸收者 | 命中 1/6 概率目标隐身 3s + 粒子 | 耐久 4095 / 伤害 12，复用已有 ghost_sickle 粒子 |
| [x] dust_destroyer 尘灭者 | 右键耗魔 1（需 >1，与基岩一致）→ 前方 3/5 格凋零雾 ×2 | 雾：接触凋零 10s 放大器2（同基岩 json）+ 2 伤害，5s 消散；耐久 2045 / 伤害 14 |
| [x] growth 生长者 | 命中耗魔 4 / 右键耗魔 5 → 生长能量射线 | 复用已有 GrowingEnergyRay；耐久 502 / 伤害 7 |

### B 批：AOE/增益/蓄力
| 剑 | 机制 | 要点 |
|---|---|---|
| [ ] everlasting_winter_sword 永冬长剑 | 右键耗魔 1 → 半径 2-4 环减速 3s I | 耐久 1712 / 伤害 8 / 附魔 5 |
| [ ] sword_of_deep 黑域 | 右键耗魔 4 → 半径 4 全体凋零 10s II | 耐久 1023 / 伤害 13；粒子 deep_range 需移植 |
| [ ] vortex 旋涡 | 右键：水中耗魔 7（潮涌 5s + r3 中毒 5s + 溺水伤害 10）/ 陆地耗魔 11（中毒 2s + 伤害 7） | 耐久 832 / 伤害 8；粒子 bubble_vortex 需移植 |
| [ ] scale_sword 鳞之刃 | 命中：水中 1/2 概率目标 r3 溺水伤害 10 + 自身潮涌 10s；右键水中耗魔 6 / 陆地耗魔 10 强化版（伤害 12/8） | 耐久 1024 / 伤害 8 |
| [ ] warden_sword 匿潜刃 | 长按 1.5s 蓄力：耗魔 20 → 自身失明 10s + 黑暗 30s + 力量 8s + 速度 8s II + 抗性 8s II | 耐久 563 / 伤害 6 / 附魔 7，冷却 15s |
| [ ] wind_of_shadow 暗影之风 | 两段：右键耗魔 10 → 隐身 + 加速 3s；续按 3s → 半径 4 魔法伤害 11 | 耐久 524 / 伤害 8，冷却 4s；粒子 ender_spurt/ender_bomb 需移植 |
| [ ] the_imperial_sword 尚方宝剑 | 右键计数蓄能：第 8 次耗魔 15 → 力量 5s II + 抗性 2s III + r2-5 定身 1s；第 16 次强化版 | 耐久 564 / 伤害 8 / 附魔 20；粒子 frozen_shield 需移植 |

### C 批：发射投射物（需新实体）
| 剑 | 投射物 | 要点 |
|---|---|---|
| [ ] blue_of_the_sea 海之忧伤 | HardBubble ×4（伤害 4、无重力、初速 3/1/0.6/0.6） | 耗魔 3，耐久 412 / 伤害 6，冷却 0.7s |
| [ ] storm_giant_sword 风暴巨剑 | StormEnergy ×4（伤害 4 + 击退、初速 0.08-0.3 散射） | 耗魔 3，耐久 533 / 伤害 6，冷却 2s |
| [ ] radiate_sword 堕落圣刃 | RadiateFog（伤害 1 + 中毒 5s III） | 耗魔 2，耐久 1024 / 伤害 10，冷却 0.2s |
| [ ] star_sword 迸发之星 | SpotsBySword ×6（伤害 14 + 点燃、初速 0.2-2.0） | 右键叠计数至 12 发射（4/8 层蓄力特效），耗魔 15；耐久 4096 / 伤害 12 / 附魔 10 |
| [ ] bamboo_yataghan 竹长剑 | 投掷剑本体（伤害 7 + 击退、重力、弹跳、插地） | 需耐久 >7 并消耗 7；耐久 60 / 伤害 6，无附魔 |
| [ ] village_guardian 村庄守卫 | 被动：命中给目标附近 4 格村民回血 10s；技能：占位（抬头+耐久>100+耗魔 20，提示等待传送门内容） | 耐久 513 / 伤害 6，冷却 60s |

### 待移植粒子清单（剑相关）
`bat_spurt`（A 批）、`deep_range`、`bubble_vortex`、`frozen_shield`、`village_portal_seep`、`ender_bomb`、`ender_spurt`、`bubble_spurt_middle`（B/C 批）

### 待新建投射物实体
`PumpkinBomb`、`WitherCloud`（A 批）、`HardBubble`、`StormEnergy`、`RadiateFog`、`SpotsBySword`、`BambooYataghanProjectile`（C 批）
