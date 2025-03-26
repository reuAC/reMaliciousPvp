# reMaliciousPvp
使用机制防止恶意Pvp

## 介绍
该插件适用于Spigot1.8及以上。  

## 机制
若玩家被连续击杀n次（可设置计数击杀中与第一次击杀的时间间隔），则询问被杀玩家是否被恶意击杀，若被确认，则触发指令。  
可以通过配置文件设置机制不生效的世界。

## 指令
`/remaliciouspvp` 重载配置。  
**缩写：`/rempvp`**

## 配置文件
插件成功启动后，会在plugins文件夹下生成配置文件，位于 `plugins/reMaliciousPvp/config.yml`  

```yaml
# reMaliciousPvp
#
# %target_player_name% 会被替换为杀死受害者的玩家的ID
# %victim_player_name% 会被替换为受害者的玩家的ID

# 该机制不生效的世界
ignoredWorlds:
  - "spawn"
  - "world"
# 连续击杀次数
consecutiveKills: 2
# 距离第一次击杀的间隔（秒）
maximumInterval: 120
# 对可能的受害者的提示
victimTips:
  - "&c是否被恶意Pvp? (回答“s”或者“是”)"
# 对可能的加害者的提示
targetTips:
  - "&c您疑似对 %victim_player_name% 恶意击杀"
# 受害者确认被恶意Pvp后发送的提示。
victimConfirmTips:
  - "&c已经囚禁 %target_player_name%"
# 受害者否认被恶意Pvp后发送的提示。
victimCancelTips:
  - "&c已经取消"
# 提示后紧接着若回答如下内容的其中一个，则触发指令
victimReply:
  - "s"
  - "S"
  - "是"
# 被触发的指令
commands:
  - "cmi jail %target_player_name%"

```

## 权限节点
`remaliciouspvp.main` 使用重载指令。

## 使用方法
1. 将插件放入plugins文件夹中，重启服务器。
