# 基岩版武器与法杖行为迁移方案

## 目标

将以下 21 个物品从当前临时适配行为，替换为基岩版 JSON、实体定义、脚本和函数文件对应的专属行为：

`angel_purification`、`bamboo_yataghan`、`bat_staff`、`blaze_staff`、`blue_of_the_sea`、`catapult`、`chaos_staff`、`chicken_staff`、`destroy_staff`、`dust_staff`、`echo_of_deep`、`god_of_destroy`、`god_of_sun`、`natural_spear`、`radiate_spreader`、`radiate_sword`、`spider_staff`、`star_sword`、`storm_giant_sword`、`village_guardian`、`wooden_enchant_book`。

迁移必须继续使用项目现有的统一物品施法、魔力、冷却、耐久、基岩模型/实体渲染系统；只有确实不存在等价实现时才新增通用组件。

## 基岩版行为分析

| 物品 | 基岩版触发 | 基岩版专属行为 | NeoForge 实现结论 |
|---|---|---|---|
| `angel_purification` | `angel_purification_use` | 发射 `angel_purification_blade`，实体速度 10、无重力、伤害 7；物品不消耗魔力，每次技能事件消耗 1 点耐久 | 新增专属投射物定义，复用统一碰撞/粒子基类；不能直接套当前 EnergyBall |
| `bamboo_yataghan` | `throw` | 投掷 `bamboo_yataghan`，物品耐久消耗 7，实体速度 3、重力 0.1；命中不立即销毁，可插地/反弹，带 0.5–2 秒引信但爆炸威力为 0 | 新增具备插地/反弹/延迟销毁语义的专属投射物；不能复用普通剑投掷逻辑 |
| `bat_staff` | `shoot` | 魔力大于 4，消耗 4，发射 `little_bat` | 复用已有实体类型和模型，但必须新增施法者归属、友军目标过滤、生命周期处理 |
| `blaze_staff` | `shoot` | 魔力大于 6，消耗 6，发射 `fire_storm` | 同上；保留已有实体渲染，补齐召唤物归属和 AI 行为 |
| `blue_of_the_sea` | `shoot` | 这是剑类物品；魔力大于 3，消耗 3，依次发射 4 个 `hard_bubble`，发射速度为 3/1/0.6/0.6 | 使用剑类主动技能适配器和专属 `HardBubble`，不能注册成法杖类 |
| `catapult` | 脚本 `afterItemUse` | 必须从背包优先消耗 `exploding_pellets`，否则消耗 `small_stone`；速度 0.9、散射 3；弹射器冷却类别受 `stones_bag`/`archer_stones_bag` 修正 | 新增 `CatapultItem`，复用项目背包和冷却修正系统；不套 `GunItem` 的火枪弹药流程 |
| `chaos_staff` | `shoot` | 魔力大于 10，消耗 10，发射 `son_of_chaos` | 复用已有实体类型和模型，但必须补齐召唤归属/友军目标过滤 |
| `chicken_staff` | `shoot` | 魔力大于 3，消耗 3，发射 `angry_chicken` | 同上 |
| `destroy_staff` | `destroy_staff_use` | 执行独立的 67 行函数，按视线坐标破坏指定方块；使用事件自身耐久消耗，不消耗魔力 | 新增服务器端方块技能配置；按该函数独立移植，不能与 `god_of_destroy` 共用方块列表 |
| `dust_staff` | `shoot` | 魔力大于 6，消耗 6，发射 `elf_of_dust` | 复用已有实体类型和模型，但必须补齐召唤归属/友军目标过滤 |
| `echo_of_deep` | `shoot` | 这是远程武器；魔力大于 15，消耗 15，发射 2 个 `bullet_by_echo_of_deep`，发射速度为 1/0.2；实体伤害 18 | 新增专属投射物和远程主动武器适配器；不能用现有 `DeepEnergy` 代替 |
| `god_of_destroy` | `god_of_destroy_use` | 执行独立的 458 行函数，破坏范围/方块表明显大于 `destroy_staff`；不消耗魔力，按事件伤害消耗耐久 | 与普通毁灭法杖共享执行框架，但使用独立数据配置，不共用函数内容 |
| `god_of_sun` | `shoot` | 魔力大于 7，消耗 7，发射 `sun_energy`；伤害 8、着火，命中触发威力 1 爆炸 | 新增 `SunEnergy`，实现实体命中和方块命中爆炸、着火效果 |
| `natural_spear` | `shoot` | 魔力大于 9，消耗 9，发射 `son_of_nature` | 复用已有实体类型和模型，但必须补齐召唤归属/友军目标过滤 |
| `radiate_spreader` | `shoot` | 魔力大于 8，消耗 8，发射 3 个 `radiate_cloud`；伤害 19，命中附加中毒 200 tick、等级 2 | 新增 `RadiateCloud`，同时接入 `radiate_wake` 粒子资源 |
| `radiate_sword` | `shoot` | 魔力大于 2，消耗 2，发射 `radiate_fog`；速度 0.4、惯性 1.1，伤害 1，附加中毒 100 tick、等级 2 | 新增 `RadiateFog`，接入对应粒子和命中效果 |
| `spider_staff` | `shoot` | 魔力大于 1，消耗 1，发射 `friendly_spider` | 复用已有实体类型和模型，但必须补齐召唤归属/友军目标过滤 |
| `star_sword` | `shoot` | 魔力不足 15 时重置蓄力；魔力至少 15 时每次使用累加 `skill_count`，第 4/8 次播放蓄力效果，第 12 次一次发射 6 个 `spots_by_sword`，速度为 0.2/0.4/0.5/0.9/1.4/2，并消耗 15 魔力 | 新增带持久化蓄力状态的剑类技能；专属 `SpotsBySword` 伤害 14、着火、插地后销毁，不能复用 `SpotsByBook` |
| `storm_giant_sword` | `storm_giant_sword_use_attack` | 魔力大于 3，消耗 3，一次发射 4 个 `storm_energy` 变体；物品事件使用 a/b/c/d，对应速度 0.08/0.1/0.2/0.3，伤害 4、重力 0.01、击退 | 新增 `StormEnergy` 变体参数；不能用 `StormFuse` 替代 |
| `village_guardian` | `village_guardian_use` | Boss 半径 64 内存在 Boss 时失败；玩家视角条件满足且耐久剩余大于 100、魔力大于 19 时，消耗 19 魔力和 70 耐久，生成 `village_portal`；普通命中给半径 4 内村民再生 10 秒 | 新增独立的视觉传送门实体和技能逻辑；复用粒子/动画资源，不复用雪传送门的维度传送逻辑 |
| `wooden_enchant_book` | `shoot` | 魔力大于 5，消耗 5，发射 `ghost_wooden_sword` | 复用已有模型资源，但必须把现有敌对 `BedrockMob` 改为可归属召唤物，保留 10 秒消失和 owner 行为 |

## 已有系统可复用情况

### 可以复用但需要扩展

- `MagicWeapon` / `ProjectileStaff`：复用右键触发、冷却和魔力检查，但必须增加一次施法扣耐久、不同发射速度、多段蓄力和生物召唤支持。
- `GeneratedMobs`：复用 8 个实体的注册和现有模型渲染；这些实体当前不是可直接用于法杖的友军召唤物，必须补充 owner 标记、目标过滤、召唤生命周期和消失逻辑。
- `ParticleRayProjectile`：只能复用基础移动/命中框架；竹偃月刀的插地/反弹、太阳能量的爆炸、星剑的插地销毁等需要可配置命中策略，不能直接照搬现有类。
- `BedrockProjectileRenderer`、`ThrownItemRenderer` 和粒子发射器：按基岩实体的 `runtime_identifier` 和资源情况选择。当前多个实体没有独立几何模型，应使用投射物贴图/物品贴图/粒子，而不是强行创建 Bedrock 几何模型。
- `GunItem` / `FlintlockBulletEntity`：只作为发射方向、冷却和实体注册的参考；`catapult` 必须复用背包弹药和 `CooldownBonus`，不能直接共用火枪弹药流程。
- 现有服务器端传送门注册流程：只复用事件与渲染注册模式；`village_portal` 是视觉实体，不是维度传送门，不能复用雪传送门的传送逻辑。

### 必须新增或重构

- 专属投射物：`angel_purification_blade`、`bamboo_yataghan`、`hard_bubble`、`bullet_by_echo_of_deep`、`sun_energy`、`radiate_cloud`、`radiate_fog`、`spots_by_sword`、`storm_energy`。
- 投射物命中策略：支持伤害、击退、着火、中毒、爆炸、插地、反弹、延迟销毁和不同惯性，不能只增加实体类而复用同一套命中行为。
- 召唤物适配层：从 `EntityType` 生成实体、设置 owner/持久化数据、设置朝向和初始运动，并通过统一目标过滤避免攻击施法者；必要时重构现有生成生物的 AI，而不是把敌对 `BedrockMob` 直接当友军。
- 统一多发射配置：支持每发独立速度、散射、实体工厂和一次性魔力/耐久消耗；星剑还需要持久化蓄力计数。
- 两套独立方块破坏数据：分别解析 `destroy_staff.mcfunction` 的 67 行和 `god_of_destroy.mcfunction` 的 458 行，保留各自坐标偏移与方块白名单，并在服务端执行。
- `catapult` 的 `CatapultItem`、普通弹/爆炸弹实体、弹药消费、背包检查和冷却类别修正。
- `village_portal` 的视觉实体、动画/粒子及短生命周期；不把它实现成真正的维度传送门。
- 缺少的实体贴图/粒子资源：先检查基岩源和 `_merge_staging`，有资源则接入统一资源管线；没有几何模型的实体使用基岩运行时对应的投射物/粒子表现。

## 关键兼容性要求

1. 召唤生物必须设置 owner 或等价的持久化归属状态，不能让法杖召唤物立即把施法者作为敌对目标；现有 `Bat`、`Blaze`、`Chicken`、`Vex`、`Spider`、`IronGolem` 子类不能仅靠调用一个 `tame()` 方法完成，必须增加统一目标过滤/归属实现。
2. 投射物必须拥有独立的实体注册、客户端渲染、碰撞箱、伤害、惯性和命中效果；不得通过复用 `EnergyBall` 贴图或伤害来占位。
3. 多发射物必须在一次施法中只扣除一次魔力，并保持基岩版的发射数量和速度差异。
4. `destroy_staff` 与 `god_of_destroy` 的方块破坏只能在服务端执行，不允许客户端直接改方块。
5. `village_guardian` 的村民治疗范围、Boss 检查、视角条件和传送门效果要分别实现，不能仅生成粒子。
6. 所有新实体使用现有 Bedrock 资源加载和统一实体渲染注册流程；基岩实体明确使用 `runtime_identifier: egg/snowball` 且没有几何模型时，使用对应的投射物贴图或粒子表现，不强行伪造几何模型，也不能新增另一套模型系统。

## 物品类型与消耗复核

基岩版分类必须保留，不能因为技能形式是“发射”就统一注册成法杖：

- 剑类：`angel_purification`、`bamboo_yataghan`、`blue_of_the_sea`、`radiate_sword`、`star_sword`、`storm_giant_sword`、`village_guardian`。
- 远程类：`catapult`、`echo_of_deep`。
- 法杖/召唤类：`bat_staff`、`blaze_staff`、`chaos_staff`、`chicken_staff`、`destroy_staff`、`dust_staff`、`god_of_destroy`、`god_of_sun`、`natural_spear`、`radiate_spreader`、`spider_staff`、`wooden_enchant_book`。

技能条件和消耗必须来自基岩事件，而不是按物品名称推断：

- 不消耗魔力但消耗耐久：`angel_purification`、`bamboo_yataghan`、`destroy_staff`、`god_of_destroy`。
- 按事件条件扣魔力：其余带 `shoot` 或技能事件的物品，具体数值以行为分析表为准。
- `star_sword` 的 15 点魔力只在第 12 次蓄力发射时扣除；蓄力期间不能提前扣除。
- `catapult` 不消耗魔力，消耗弹药物品；弹药优先级为 `exploding_pellets` → `small_stone`。

## 实施顺序

1. 先修正物品分类和基础消耗规则：蓝之海/辐射之剑/星辰之剑/风暴巨剑/村庄守护者应保持剑类；深渊回影和弹射器应保持远程类；天使净化不扣魔力。
2. 新增可配置投射物基础和命中策略，先覆盖 9 类专属投射物。
3. 新增召唤物归属与 AI 适配，再接入 8 个召唤类物品。
4. 新增 `CatapultItem`、弹药检查、两种弹丸和冷却修正。
5. 分别移植 `destroy_staff`、`god_of_destroy`，再实现 `village_portal` 视觉技能。
6. 实现星辰之剑的 12 次蓄力流程和风暴巨剑的 4 个速度变体。
7. 替换 `Weapon.kt` 中当前临时适配注册；同时补齐投射物资源和客户端渲染注册。
8. 执行 `runData`、编译和启动测试；检查物品分类、物品栏模型、施法、弹药、实体归属、实体渲染、碰撞、伤害、魔力、耐久和冷却。

## 当前结论

当前项目中的 `BedrockConfiguredStaff`、`BedrockConfiguredWeapon` 只能作为临时兼容层，不能满足这批物品的最终移植要求。特别是：

- 当前 `blue_of_the_sea` 被临时注册成法杖，但基岩版明确是剑类，并且四发速度不同。
- 当前 `star_sword` 被临时实现成一次性多发射，缺少 12 次蓄力状态。
- 当前 `storm_giant_sword` 的临时射弹不是 `storm_energy`，不能使用 `StormFuse` 代替。
- 当前 `catapult` 是普通物品，不能还原基岩脚本的背包弹药逻辑。
- 当前 `destroy_staff` 与 `god_of_destroy` 不能共用同一份方块列表；两份函数分别是 67 行和 458 行。
- 当前已注册的召唤生物主要按独立敌对生物实现，不能直接视为已经完成 owner/友军行为。

因此最终实现方案确定为：以现有统一物品和 Bedrock 渲染基础设施为底层，新增可配置投射物命中策略、召唤物归属适配器、独立弹射器物品、两套独立方块技能数据以及星辰之剑蓄力状态；不再使用 `EnergyBall`、`DeepEnergy`、`SpotsByBook` 或 `StormFuse` 作为这些物品的最终替代行为。

## 复核依据与资源检查

- 物品事件来源：`_tmp_bedrock_source/DecIslandB/ex_items/*.json`。
- 投射物/召唤物行为来源：`_tmp_bedrock_source/DecIslandB/entities/*.json`。
- 方块技能来源：`_tmp_bedrock_source/DecIslandB/functions/item/destroy_staff.mcfunction` 与 `god_of_destroy.mcfunction`。
- 弹射器实际发射逻辑来源：`_tmp_bedrock_source/DecIslandB/scripts/ExcellentMInecraftscripts/src/dec/server/DecClient.ts` 的 `afterItemUse` 分支。
- 当前项目已经存在部分实体语言、召唤物模型和弹药袋，但缺少上述专属投射物的完整 NeoForge 注册/行为/客户端注册；资源迁移必须和实体实现一起检查，不能只新增 Java 类。

