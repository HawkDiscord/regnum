# Regnum server
 [![Ci](https://ci.schlaubi.me/app/rest/builds/buildType:(id:Regnum_Build)/statusIcon)](https://ci.schlaubi.me/viewType.html?buildTypeId=Regnum_Build&guest=1)
 
Regnum server is a Javalin based websocket server used for balancing Discord shard on Regnum nodes.
## Index
 - [Download](#download)
 - [Usage](#usage)
 - [Plugins](#plugins)
 - [REST API](#rest-api)
### Download
You can download Regnum artifacts from the [GitHub releases]() or from [the CI]() 
### Usage
In order to run a copy of the Regnum server you need all of the following requirements:

- Two registered Discord applications (one for the built-in Discord bot and one for the nodes)

Optional requirements:

- An running [Cassandra](https://cassandra.apache.org) instance (needed for REST API)
- An running [Sentry](https://sentry.io) instance or hosted organisation (Needed for error logging)

Configuration:

| Configuration node             	| type           	| Description                                                                   	| Optional                                                             	|
|--------------------------------	|----------------	|-------------------------------------------------------------------------------	|----------------------------------------------------------------------	|
| `socket.port`                  	| `int`          	| The port for the websocket and rest api                                       	| no                                                                   	|
| `socket.token`                 	| `String`       	| The token used for websocket authentication                                   	| no, except you use your own implementation of `AuthorizationHandler` 	|
| `socket.heartbeat`             	| `long`         	| Interval in seconds for heartbeat                                             	| no                                                                   	|
| `socket.identify_timeout`      	| `long`         	| Time in seconds the server waits till the node sends an `IDENTIFY` packet     	| no                                                                   	|
| `discord.token`                	| `String`       	| Token for built-in Discord bot                                                	| no, except you disable the Discord bot                               	|
| `discord.prefix`               	| `String`       	| The prefix for built-in Discord bot commands                                  	| no, except you disable the Discord bot                               	|
| `discord.owners`               	| `List<Long>`   	| List of ids which are allowed to use commands                                 	| no, except you disable the Discord bot                               	|
| `discord.node_token`           	| `String`       	| Token used for nodes                                                          	| no                                                                   	|
| `general.plugins_directory`    	| `String`       	| Directory which contains plugins                                              	| no                                                                   	|
| `general.load_balance_timeout` 	| `long`         	| Time in seconds the server waits for nodes to connect before balancing shards 	| no                                                                   	|
| `cassandra.contact_points`     	| `List<String>` 	| List of Cassandra contact points                                              	| no                                                                   	|
| `contacts.username`            	| `String`       	| Cassandra username                                                            	| no                                                                   	|
| `contacts.password`            	| `String`       	| Cassandra password                                                            	| no                                                                   	|
| `cassandra.keyspace`           	| `String`       	| Cassandra keyspace                                                            	| no                                                                   	|
| `sentry.dsn`                   	| `String`       	| Sentry DSN used for error logging                                             	| no, exept you disable Sentry                                         	|

Startup flags:

|        	|                    	|                                                                                                           	|                                   	|          	|
|--------	|--------------------	|-----------------------------------------------------------------------------------------------------------	|-----------------------------------	|----------	|
|        	|                    	|                                                                                                           	|                                   	|          	|
|        	|                    	|                                                                                                           	|                                   	|          	|
| Option 	| Long option        	| Type                                                                                                      	| Description                       	| Optional 	|
| `-D`   	| `-debug`           	| `Takes no argument`                                                                                       	| Enables debug mode                	| yes      	|
| `-L`   	| `--log-level`      	| [`Level`](https://github.com/qos-ch/slf4j/blob/master/slf4j-api/src/main/java/org/slf4j/event/Level.java) 	| Sets the root log level           	| yes      	|
| `-ND`  	| `--no-discord`     	| `Takes no argument`                                                                                       	| Disables the built-in Discord bot 	| yes      	|
| `-DS`  	| `--disable-sentry` 	| `Takes no argument`                                                                                       	| Disables Sentry                   	| yes      	|
| `-DA`  	| `--disable-api`    	| `Takes no argument`                                                                                       	| Disable Cassandra and REST API    	| yes      	|



### Plugins
You can find out more about plugin support [here](https://github.com/HawkDiscord/regnum/tree/master/plugin)
### REST API
As long as Regnum server is connected to a Cassandra instance it can provide an REST API. You can find the documentation for it [here](https://docs.hawkbot.cc)