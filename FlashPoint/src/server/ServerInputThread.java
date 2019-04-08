package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import actions.ActionList;
import chat.ChatMsgEntity;
import commons.bean.TextMessage;
import commons.bean.User;
import commons.tran.bean.TranObject;
import commons.tran.bean.TranObjectType;
import commons.util.Constants;
import commons.util.MyDate;
import game.GameState;
import lobby.Lobby;
import tile.Tile;
import token.Firefighter;
import token.Speciality;
import token.Vehicle;


public class ServerInputThread extends Thread {
	private Socket socket;
	private OutputThread out;
	private OutputThreadMap map;
	private ObjectInputStream ois;
	private boolean isStart = true;
	ServerManager serverManager;
	Random rand = new Random();
	private Object tempory;

	public ServerInputThread(Socket socket, OutputThread out, OutputThreadMap map, ServerManager newServerManager) {
		this.socket = socket;
		this.out = out;
		this.map = map;
		this.serverManager = newServerManager;
		try {
			ois = new ObjectInputStream(socket.getInputStream());// å®žé”Ÿæ–¤æ‹·é”Ÿæ–¤æ‹·é”Ÿæ–¤æ‹·é”Ÿæ–¤æ‹·é”Ÿæ–¤æ‹·é”Ÿï¿½
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}

	@Override
	public void run() {
//		serverManager = new ServerManager();
		try {
			while (isStart) {
				//System.out.println("Looping?");
//				serverManager.readMessage(out, ois);
				// JUNHA : this is supposed to be serverManager.readMessage();
				 readMessage(null);

			}
			if (ois != null)
				System.out.println("Object Input Stream is null");
			ois.close();
			if (socket != null)
				System.out.println("Socket is null error");
			socket.close();
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFound Exception");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO Exception Error");
			this.interrupt();
			try {
				serverManager.saveGame();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			TranObject<User> returnObject;
			returnObject = new TranObject<User>(TranObjectType.ERROR);
			for(Player p: serverManager.getLobby().getPlayers()) {
				OutputThread onOut = map.getById(p.getID());
				onOut.setMessage(returnObject);
			}
			e.printStackTrace();
		}

	}

	/**
	 * This method has been removed to ServerManager
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */

	public void readMessage(Object o) throws IOException, ClassNotFoundException {
//		while (true) {
//		socket.sendUrgentData(0xFF); // å�‘é€�å¿ƒè·³åŒ…
//		System.out.println("ç›®å‰�æ˜¯å¤„äºŽé“¾æŽ¥çŠ¶æ€�ï¼�");
//		
//} catch (Exception e) {
//	System.out.println("ç›®å‰�æ˜¯å¤„äºŽæ–­å¼€çŠ¶æ€�ï¼�");
//			e.printStackTrace();
		Object readObject;
//				}
		if(o == null) {
			readObject = ois.readObject();
		}
		else {
			readObject = o;
		}
//		Object readObject = ois.readObject();// é”Ÿæ–¤æ‹·é”Ÿæ–¤æ‹·é”Ÿå�«è®¹æ‹·å�–é”Ÿæ–¤æ‹·é”Ÿæ–¤æ‹·
//		System.out.println(ois.readObject());
//		try{ 
//			socket.sendUrgentData(0xFF); 
//		}
//		catch(Exception ex){ 
//			for (OutputThread onOut : map.getAll()) {
//			
//				TranObject read_tranObject2 = (TranObject) readObject;
//				TranObject<User> returnObject2;
//				User requestObject2;
//				returnObject2 = new TranObject<User>(TranObjectType.ERROR);
//				
//				onOut.setMessage(returnObject2);
//			}
//		}
		System.out.println("Insinde readMessage for Thread No # = " + this.getId());
//		UserDao dao = UserDaoFactory.getInstance();// é€šé”Ÿæ–¤æ‹·daoæ¨¡å¼�é”Ÿæ–¤æ‹·é”Ÿæ–¤æ‹·é”Ÿæ•™ï¿½
		if (readObject != null && readObject instanceof TranObject) {
		//	System.out.println("Entered IF");
			TranObject read_tranObject = (TranObject) readObject;// è½¬é”Ÿæ–¤æ‹·é”Ÿç¼´è¾¾æ‹·é”Ÿæ–¤æ‹·é”Ÿæ–¤æ‹·é”Ÿï¿½
			TranObject<User> returnObject;
			TranObject<GameState> returnGameState;
			TranObject<List> returnChat;
			User requestObject;
			int idGenerator;
			switch (read_tranObject.getType()) {
			case CONNECT:
				System.out.println("In connect request");
				returnObject = new TranObject<User>(TranObjectType.SUCCESS);
				requestObject = (User) read_tranObject.getObject();
				idGenerator = rand.nextInt(10);
				while(serverManager.getPlayers().containsKey(Integer.valueOf(idGenerator))) {
					idGenerator = rand.nextInt(10);
				}
				requestObject.setId(Integer.valueOf(idGenerator));
				returnObject.setObject(requestObject);
				out.setMessage(returnObject);
				break;
			case LOGIN:
				System.out.println("In login request");
				User loginUser = (User) read_tranObject.getObject();
				
				requestObject = (User) read_tranObject.getObject();

				if(serverManager.getAccounts().get(requestObject.getName()) != null&&serverManager.getAccounts().get(requestObject.getName()).equals(requestObject.getPassword())) {
					System.out.println(serverManager.getAccounts().get(requestObject.getName()));
						System.out.println("is online ");
						requestObject.setIsOnline(1);	
						serverManager.createPlayer(requestObject.getName(), requestObject.getPassword(), requestObject.getId());
						
					returnObject = new TranObject<User>(TranObjectType.LOGINSUCCESS);
					returnObject.setObject(requestObject);
					
					out.setMessage(returnObject);
					map.add(loginUser.getId(), out);
					System.out.println("w123123123123123123123213!");
					}
				
				else { //User doesn't exist
					//System.out.println("is online set to 0");
				System.out.println("weeeeee are here!");
					requestObject.setIsOnline(0);
					returnObject = new TranObject<User>(TranObjectType.LOGINSUCCESS);
					returnObject.setObject(requestObject);
					
					out.setMessage(returnObject);
				
				}
				
				break;
			case REGISTER:
				System.out.println("In register request");
				returnObject = new TranObject<User>(TranObjectType.REGISTERSUCCESS);
				requestObject = (User) read_tranObject.getObject();
				if(serverManager.getAccounts().containsKey(requestObject.getName())) {
					System.out.println("account already exists");
					requestObject.setIsRegistered(false);
				}else {
					System.out.println("account added");
					serverManager.getAccounts().put(requestObject.getName(), requestObject.getPassword());
					System.out.println(requestObject.getPassword());
					requestObject.setIsRegistered(true);
				}
				returnObject.setObject(requestObject);
				out.setMessage(returnObject);
				break;
			case CHATMESSAGE:
				System.out.println("chat message received");
				returnObject=new TranObject<User>(TranObjectType.CHATMESSAGE);
				requestObject = (User) read_tranObject.getObject();
				
				serverManager.getChatArray().add(requestObject.getChat());
				for(ChatMsgEntity m : serverManager.getChatArray()) {
					System.out.println(m.getName() + ":" + m.getMessage());
				}
				requestObject.setChatArray(serverManager.getChatArray());
				
				returnObject.setObject(requestObject);
				for(Player p: serverManager.getLobby().getPlayers()) {
					OutputThread onOut = map.getById(p.getID());
					onOut.setMessage(returnObject);
				}
				break;
			case LOADSAVE:
				System.out.println("load request received");
				requestObject = (User) read_tranObject.getObject();
				serverManager.setLobby(requestObject.getCurrentLobby());
				serverManager.addPlayerToLobby(serverManager.getPlayer(requestObject.getId()));
				requestObject.setCurrentLobby(serverManager.getLobby());
				//serverManager.getLobby().setCapacity(serverManager.getGameState().getListOfPlayers().size());
				//serverManager.addPlayerToLobby(serverManager.getPlayer(requestObject.getId()));  //
				//serverManager.getLobby().setDifficulty("serverManager.getGameState().getDifficulty()");
				//serverManager.getLobby().setMode("mode");
				returnObject = new TranObject<User>(TranObjectType.LOADSAVESUCCESS);
				
				returnObject.setObject(requestObject);
				out.setMessage(returnObject);
				break;
			
			case STARTGAMESTATE:
				//System.out.println("In game state update request");
//				returnObject = new TranObject<User>(TranObjectType.STARTGAMESTATESUCCESS);
				returnGameState = new TranObject<GameState>(TranObjectType.STARTGAMESTATESUCCESS);
				serverManager.createGame();
//				requestObject = (User) read_tranObject.getObject();
//				requestObject.setCurrentState(serverManager.getGameState());
//				returnObject.setObject(requestObject);
				returnGameState.setObject(serverManager.getGameState());
//				out.setMessage(returnGameState);
				for (OutputThread onOut : map.getAll()) {
					onOut.setMessage(returnGameState);
				}
				break;
			case STARTSAVEDGAMESTATE:
				requestObject = (User) read_tranObject.getObject();
				System.out.println(requestObject.getNum());
				serverManager.loadGameMat(requestObject.getLoadIndex());
				returnGameState = new TranObject<GameState>(TranObjectType.STARTSAVEDGAMESTATESUCCESS);
				returnGameState.setObject(serverManager.getGameState()); // this was already done earlier
				for (OutputThread onOut : map.getAll()) {
					onOut.setMessage(returnGameState);
				}
				break;
			case FIREFIGHTERPLACEMENT:
				System.out.println("firefighter placement request");
				requestObject = (User) read_tranObject.getObject();
				int[] coords = requestObject.getCoords();
				serverManager.placeFirefighter(coords, requestObject.getId());
				returnGameState = new TranObject<GameState>(TranObjectType.FFPLACEMENTSUCCESS);
				returnGameState.setObject(serverManager.getGameState());
//				out.setMessage(returnGameState);
				for(Player p: serverManager.getGameState().getListOfPlayers()) {
					OutputThread onOut = map.getById(p.getID());
					onOut.setMessage(returnGameState);
				}
//				for (OutputThread onOut : map.getAll()) {
//					onOut.setMessage(returnGameState);
//					System.out.println(java.lang.Thread.activeCount());
//				}
//				

				break;
			case ACTIONREQUEST:
				System.out.println("In action request");
				requestObject = (User) read_tranObject.getObject();
				
				if(requestObject.getAction().getTitle() == ActionList.Drive) {
					boolean popUpFlag;
					boolean endTurnerInvolved;
					System.out.println("hello we are riding");
					if(requestObject.getAction().isAmbulance()) {
						popUpFlag = serverManager.askRelevantFirefighters(Vehicle.Ambulance);
					}
					else {
						popUpFlag = serverManager.askRelevantFirefighters(Vehicle.Engine);
					}
					
					if(popUpFlag) {
						System.out.println("we have asked");
						returnGameState = new TranObject<GameState>(TranObjectType.SENDRIDERECEIVED);
						returnGameState.setObject(serverManager.getGameState());
						for (OutputThread onOut : map.getAll()) {
							System.out.println("hello should be in the output thread");
							onOut.setMessage(returnGameState); 
						}
					}
					else {
						System.out.println("We didn't have to ask");
					}

					System.out.println("hello should be at the while" + serverManager.hasEveryoneResponded());
					if(popUpFlag) {
						endTurnerInvolved = serverManager.isEndTurnerInvolved(serverManager.getPlayer(requestObject.getId()).getFirefighters());
						if(!endTurnerInvolved) {
							while(!serverManager.hasEveryoneResponded()) {
								
							}
						}
						else {
							while(!serverManager.hasEveryoneResponded()) {
								tempory = ois.readObject();
								if(tempory != null) {
									System.out.println("Trying to read now");
									try {
										readMessage(tempory);
									}
									catch(Exception e) {
										System.out.println("EXCEPTION HAPPENED!");
									}
								}
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
//						while(true) {
//							try {
//								Thread.sleep(15000);
//							} catch (InterruptedException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//							while(!serverManager.hasEveryoneResponded()) {
//								
//							}
//							break;
//						}
//						while(!serverManager.hasEveryoneResponded()) {
//							try {
//								Thread.sleep(10000);
//							} catch (InterruptedException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//							readMessage();
//							tempory = ois.readObject();
//							if(tempory != null) {
//								System.out.println("Trying to read now");
//								try {
//									readMessage(tempory);
//								}
//								catch(Exception e) {
//									System.out.println("EXCEPTION HAPPENED!");
//								}
//							}
//							try {
//								Thread.sleep(1000);
//							} catch (InterruptedException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//						}
						System.out.println("Out of response checking loops");
					}
				}
				
//				ArrayList<Firefighter> tempList = serverManager.getPlayer(requestObject.getId()).getFirefighters();
				
				serverManager.performAction(requestObject.getAction());
				if (requestObject.getAction().getTitle() == ActionList.Drive) {
					serverManager.resetHashMap();
				}
				returnGameState = new TranObject<GameState>(TranObjectType.ACTIONSUCCESS);
				returnGameState.setObject(serverManager.getGameState());
				for(Player p: serverManager.getGameState().getListOfPlayers()) {
					OutputThread onOut = map.getById(p.getID());
					onOut.setMessage(returnGameState);
				}
				break;
			case SENDRIDERESPONSE:
				requestObject = (User) read_tranObject.getObject();
				serverManager.updateResponse(requestObject.getRideResponse(), requestObject.getMyFFIndex());
				System.out.println("hello we have updated riderResponse");
//				returnGameState = new TranObject<GameState>(TranObjectType.RIDERESPONSEACK);
//				returnGameState.setObject(serverManager.getGameState());
//				for (OutputThread onOut : map.getAll()) {
//					onOut.setMessage(returnGameState); 
//				}
				break;
			case LOBBYCREATION:
				//System.out.println("In lobby creation");
				returnObject = new TranObject<User>(TranObjectType.LOBBYCREATIONSUCCESS);
				requestObject = (User) read_tranObject.getObject();
				serverManager.setLobby(requestObject.getCurrentLobby());
				serverManager.addPlayerToLobby(serverManager.getPlayer(requestObject.getId()));
				requestObject.setCurrentLobby(serverManager.getLobby());
				System.out.println(requestObject.getCurrentLobby().getPlayers().get(0).getUserName());
				returnObject.setObject(requestObject);
				out.setMessage(returnObject);
				break;
			case FINDLOBBY:
				System.out.println("In find lobby");
				returnObject = new TranObject<User>(TranObjectType.FINDLOBBYSUCCESS);
				requestObject = (User) read_tranObject.getObject();
				requestObject.setLobbyList(serverManager.getLobbyList());
				returnObject.setObject(requestObject);
				out.setMessage(returnObject);
				break;
			case JOINLOBBY:
//				System.out.println("In join lobby");
//				returnObject = new TranObject<User>(TranObjectType.JOINLOBBYSUCCESS);
//				requestObject = (User) read_tranObject.getObject();
//				serverManager.addPlayerToLobby(serverManager.getPlayer(requestObject.getId()));
//				requestObject.setCurrentLobby(serverManager.getLobby());
//				returnObject.setObject(requestObject);
//				System.out.println("test 2 this should be entered username of second user" + requestObject.getCurrentLobby().getPlayers().get(1).getUserName());
////				out.setMessage(returnObject);
//				for (OutputThread onOut : map.getAll()) {
//					onOut.setMessage(returnObject);// å¹¿æ’­ä¸€ä¸‹ç”¨æˆ·ä¸Šçº¿
//				}
//				break;
				
				System.out.println("request join lobby");
				TranObject returnLobby = new TranObject<Lobby>(TranObjectType.JOINLOBBYSUCCESS);
				requestObject = (User) read_tranObject.getObject();
				serverManager.addPlayerToLobby(serverManager.getPlayer(requestObject.getId()));
//				requestObject.setCurrentLobby(serverManager.getLobby());
				returnLobby.setObject(serverManager.getLobby());
//				for (OutputThread onOut : map.getAll()) {
//					onOut.setMessage(returnLobby);// å¹¿æ’­ä¸€ä¸‹ç”¨æˆ·ä¸Šçº¿
//				}
				for(Player p : serverManager.getLobby().getPlayers()) {
					map.getById(p.getID()).setMessage(returnLobby);;
				}
				break;
			case ENDTURN:
				System.out.println(" end turn");
				TranObject returnGameStateEnd = new TranObject<GameState>(TranObjectType.ENDTURNSUCCESS);
				requestObject = (User) read_tranObject.getObject();
				//serverManager.endTurn();
				
				//moving advanced fire here
				System.out.println("starting advance fire new\n\n\n\n");
				//looper set to false in family, true if the initial tile has a hot spot
				boolean hasHotSpot = true;
				//used to know if the loop is on the first iteration or not
				boolean additionalHotSpot;
				int count = 0;
				serverManager.getGameManager().setAdvFire("");
				//checking if a vet exists in the game
				boolean dodgeCheck = false;
				int testCounter = 0;
				if(serverManager.getGameState().isExperienced()) {
					for(Firefighter f : serverManager.getGameState().getFireFighterList()) {
						if(f.getSpeciality() == Speciality.VETERAN) {
							dodgeCheck = true;
							System.out.println("deodgecheck" + dodgeCheck);
						}
					}
				}
				
		    	//gs.endTurn();
		    	while(hasHotSpot) {
		    		additionalHotSpot = 0<count;
		    		Tile targetTile = serverManager.getGameState().rollForTile();
		        	if(serverManager.getGameState().isExperienced()) {
		        		hasHotSpot = targetTile.containsHotSpot();
		        	} else {
		        		hasHotSpot = false;
		        	}
		        	
		        	if(serverManager.getGameManager().advanceFireStart(targetTile, additionalHotSpot) && dodgeCheck) {
		        		System.out.println("dodge triggered after start");
		        		if(serverManager.generateDodgeActions()) {
		        			System.out.println("we have asked dodge");
							returnGameState = new TranObject<GameState>(TranObjectType.DODGERECEIVED);
							returnGameState.setObject(serverManager.getGameState());
							for (OutputThread onOut : map.getAll()) {
								System.out.println("hello should be in the output thread of dodge");
								onOut.setMessage(returnGameState); 
							}
							System.out.println("Exited sending output thread to everyone");
							while(true) {
								if(serverManager.hasEveryoneDodged()) {
									break;
								}
								tempory = ois.readObject();
								if(tempory != null) {
									System.out.println("Trying to read now");
									try {
										readMessage(tempory);
									}
									catch(Exception e) {
										System.out.println("EXCEPTION HAPPENED!");
									}
								}
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}

//							readMessage();
//							while(!serverManager.hasEveryoneDodged()) {
//							
//							}
							System.out.println("Everyone has responded");
		        		}
		        	}
		        	
		        	if(serverManager.getGameManager().resolveFlashOver() && dodgeCheck) {
		        		System.out.println("dodge triggered after flashover");
		        		if(serverManager.generateDodgeActions()) {
		        			System.out.println("we have asked dodge");
		        			returnGameState = new TranObject<GameState>(TranObjectType.DODGERECEIVED);
							returnGameState.setObject(serverManager.getGameState());
							for (OutputThread onOut : map.getAll()) {
								System.out.println("hello should be in the output thread of dodge");
								onOut.setMessage(returnGameState); 
							}
							
							System.out.println("Exited sending output thread to everyone");
							while(true) {
								if(serverManager.hasEveryoneDodged()) {
									break;
								}
									tempory = ois.readObject();
									if(tempory != null) {
										System.out.println("Trying to read now");
										try {
											readMessage(tempory);
										}
										catch(Exception e) {
											System.out.println("EXCEPTION HAPPENED!");
										}
									}
									try {
										Thread.sleep(1000);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
							}
	//							readMessage();
//							while(!serverManager.hasEveryoneDodged()) {
//	
//							}
							System.out.println("Everyone has responded");
		        		}
		        	}
		        	
		        	if(serverManager.getGameState().isExperienced()) {
		        		if(serverManager.getGameManager().resolveHazmatExplosions() && dodgeCheck) {
		        			System.out.println("dodge triggered after hazmatExplosion");
		        			if(serverManager.generateDodgeActions()) {
		        				System.out.println("we have asked dodge");
			        			returnGameState = new TranObject<GameState>(TranObjectType.DODGERECEIVED);
								returnGameState.setObject(serverManager.getGameState());
								for (OutputThread onOut : map.getAll()) {
									System.out.println("hello should be in the output thread of dodge");
									onOut.setMessage(returnGameState); 
								}
								
								System.out.println("Exited sending output thread to everyone");
								while(true) {
									if(serverManager.hasEveryoneDodged()) {
										break;
									}
									tempory = ois.readObject();
									if(tempory != null) {
										System.out.println("Trying to read now");
										try {
											readMessage(tempory);
										}
										catch(Exception e) {
											System.out.println("EXCEPTION HAPPENED!");
										}
									}
									try {
										Thread.sleep(1000);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
//								readMessage();
//								while(!serverManager.hasEveryoneDodged()) {
//									
//								}
								System.out.println("Everyone has responded");
			        		}
		        		}
		        	}
		        	
		        	if(additionalHotSpot) {
		        		if(serverManager.getGameState().getHotSpot()>0) {
		    				targetTile.setHotSpot(1);
		    				serverManager.getGameState().setHotSpot(serverManager.getGameState().getHotSpot() - 1);
		    				serverManager.getGameManager().setAdvFire("hotSpot added to final tile at coords: " + targetTile.getCoords()[0] + "," + targetTile.getCoords()[1]  +"\n");
		    			}
		        	}
		        	
		        	count++;
		        	if(hasHotSpot) {
		        		serverManager.getGameManager().setAdvFire("hotSpot triggered another advanceFire \n");
		        	}
		    	}
		    	
		    	serverManager.getGameManager().checkKnockDowns();
		    	serverManager.getGameManager().placePOI();
		    	serverManager.getGameManager().clearExteriorFire();
		    	
		    	int wallCheck = serverManager.getGameState().getDamageCounter();//should this running the same time with the main process? @Eric
		    	int victimCheck = serverManager.getGameState().getLostVictimsList().size();
		    	int savedVictimCheck = serverManager.getGameState().getSavedVictimsList().size();
		    	
		    	
		    	if(wallCheck >= 24 || victimCheck >= 4) {
		    		serverManager.getGameState().terminateGame();
		    	} else if(savedVictimCheck >= 7) {
		    		serverManager.getGameState().winGame();
		    	}
		    	serverManager.getGameState().setAdvFireString(serverManager.getGameManager().getAdvFireMessage());
		    	
				serverManager.setFFNextTurn();
				serverManager.generateActions();
				//ending advanced fire here
				
				System.out.println("end fo advance fire");
				
				returnGameStateEnd.setObject(serverManager.getGameState());
				for(Player p: serverManager.getGameState().getListOfPlayers()) {
					OutputThread onOut = map.getById(p.getID());
					onOut.setMessage(returnGameStateEnd);
				}
				
//				for(OutputThread onOut : map.getAll()) {
//					onOut.setMessage(returnGameStateEnd);
//				}
				break;
			case DODGERESPONSE:
				System.out.println("hello we have updated dodge Response");
				requestObject = (User) read_tranObject.getObject();
				serverManager.updateDodgeRespone(requestObject.getDodgeAction(), requestObject.getMyFFIndex());
				
				break;
			case SAVEGAME:
				System.out.println("save game");
				returnObject = new TranObject<User>(TranObjectType.SAVEGAMESUCCESS);
				requestObject = (User) read_tranObject.getObject();
				//@CAO this is where you would save the game state
				serverManager.saveGame();
				
				returnObject.setObject(requestObject);
				out.setMessage(returnObject);
				break;
				
			case SAVEGAMENAME:
				System.out.println("save game with name");
				returnObject = new TranObject<User>(TranObjectType.SAVEGAMENAMESUCCESS);
				requestObject = (User) read_tranObject.getObject();
				String name = (String) requestObject.getGameName();
				System.out.println("WE ARE HERE!" + name);
				serverManager.getGameState().setSavedGameName(name);
				serverManager.saveGameMat(serverManager.getGameState(), name);
				serverManager.setSavedGames();
				requestObject.setSavedGameStates(serverManager.getSavedGames());
				
			case REQUESTSAVEDLIST:
				System.out.println("request saved game state objects");
				returnObject = new TranObject<User>(TranObjectType.REQUESTSAVEDLISTSUCCESS);
				requestObject = (User) read_tranObject.getObject();
				serverManager.setSavedGames();
				requestObject.setSavedGameStates(serverManager.getSavedGames());

				System.out.println("list length is: " + requestObject.getsavedGameStates().size() );
				returnObject.setObject(requestObject);
				out.setMessage(returnObject);
				break;
				
			case MESSAGE:
				
				
				
				
				for (OutputThread onOut : map.getAll()) {
					onOut.setMessage(read_tranObject);
				}
				
					
				
			
				break;
			case VEHICLEPLACEMENT:
				System.out.println("Vehicle placement request");
				requestObject = (User) read_tranObject.getObject();
				serverManager.placeVehicle(requestObject.getVehicleIndex(), requestObject.getVehicleType());
				returnGameState = new TranObject<GameState>(TranObjectType.VEHICLEPLACEMENTSUCCESS);
				returnGameState.setObject(serverManager.getGameState());

				for(Player p: serverManager.getGameState().getListOfPlayers()) {
					OutputThread onOut = map.getById(p.getID());
					onOut.setMessage(returnGameState);
				}
				break;
			case SPECIALITYSELECTREQUEST:
				System.out.println("Speciality selection request");
				requestObject = (User) read_tranObject.getObject();
				serverManager.setSpeciality(requestObject, requestObject.getDesiredSpeciality() );
				returnGameState = new TranObject<GameState>(TranObjectType.SPECIALITYSELECTED);
				returnGameState.setObject(serverManager.getGameState());
				for(Player p: serverManager.getGameState().getListOfPlayers()) {
					OutputThread onOut = map.getById(p.getID());
					onOut.setMessage(returnGameState);
				}
				break;
			case SELECTENDREQUEST:
				System.out.println("Speciality end request");
				requestObject = (User) read_tranObject.getObject();
				serverManager.setSpecialitySelecting(false);
				returnGameState = new TranObject<GameState>(TranObjectType.SPECIALITYENDSUCCESS);
				returnGameState.setObject(serverManager.getGameState());
				for(Player p: serverManager.getGameState().getListOfPlayers()) {
					OutputThread onOut = map.getById(p.getID());
					onOut.setMessage(returnGameState);
				}
				break;
			case FIREFIGHTERSELECTREQUEST:
				System.out.println("Firefighter selection request");
				requestObject = (User) read_tranObject.getObject();
				serverManager.setFirefighter(requestObject);
				returnGameState = new TranObject<GameState>(TranObjectType.FIREFIGHTERSELECTED);
				returnGameState.setObject(serverManager.getGameState());
				for(Player p: serverManager.getGameState().getListOfPlayers()) {
					OutputThread onOut = map.getById(p.getID());
					onOut.setMessage(returnGameState);
				}
				break;
			case ROLLDICEFORME:
				System.out.println("Rolling Dice for Driver request");
				requestObject = (User) read_tranObject.getObject();
				serverManager.setDice();
				returnGameState = new TranObject<GameState>(TranObjectType.FIREFIGHTERSELECTED);
				returnGameState.setObject(serverManager.getGameState());
				for(Player p: serverManager.getGameState().getListOfPlayers()) {
					OutputThread onOut = map.getById(p.getID());
					onOut.setMessage(returnGameState);
				}
				break;
			case ROLLREDDICEFORME:
				System.out.println("ReRolling Red Dice for Driver request");
				requestObject = (User) read_tranObject.getObject();
				serverManager.setDice(0);
				returnGameState = new TranObject<GameState>(TranObjectType.FIREFIGHTERSELECTED);
				returnGameState.setObject(serverManager.getGameState());
				for (OutputThread onOut : map.getAll()) {
					onOut.setMessage(returnGameState);
				}
				break;
			case ROLLBLACKDICEFORME:
				System.out.println("ReRolling Black Dice for Driver request");
				requestObject = (User) read_tranObject.getObject();
				serverManager.setDice(1);
				returnGameState = new TranObject<GameState>(TranObjectType.FIREFIGHTERSELECTED);
				returnGameState.setObject(serverManager.getGameState());
				for (OutputThread onOut : map.getAll()) {
					onOut.setMessage(returnGameState);
				}
				break;
				
			case LOGOUT:
				User logoutUser = (User) read_tranObject.getObject();
				int offId = logoutUser.getId();
				System.out
						.println(MyDate.getDateCN() + " ç”¨æˆ·ï¼š" + offId + " ä¸‹çº¿äº†");
			
				isStart = false;
				map.remove(offId);// ä»Žç¼“å­˜çš„çº¿ç¨‹ä¸­ç§»é™¤
				out.setMessage(null);// å…ˆè¦�è®¾ç½®ä¸€ä¸ªç©ºæ¶ˆæ�¯åŽ»å”¤é†’å†™çº¿ç¨‹
				out.setStart(false);// å†�ç»“æ�Ÿå†™çº¿ç¨‹å¾ªçŽ¯

				TranObject<User> offObject = new TranObject<User>(
						TranObjectType.LOGOUT);
				User logout2User = new User();
				logout2User.setId(logoutUser.getId());
				offObject.setObject(logout2User);
				for (OutputThread offOut : map.getAll()) {// å¹¿æ’­ç”¨æˆ·ä¸‹çº¿æ¶ˆæ�¯
					offOut.setMessage(offObject);
				}
				break;
//			case REGISTER:// é”Ÿæ–¤æ‹·é”Ÿæ–¤æ‹·æ²¡é”Ÿæ–¤æ‹·é”Ÿé˜¶î�®æ‹·é”Ÿï¿½
//				User registerUser = (User) read_tranObject.getObject();
////				int registerResult = dao.register(registerUser);
//				System.out.println(MyDate.getDateCN() + " é”Ÿæ–¤æ‹·é”ŸçŸ«ä¼™æ‹·æ³¨é”Ÿæ–¤æ‹·:"
////						+ registerResult);
//				// é”Ÿæ–¤æ‹·é”ŸçŸ«ä¼™æ‹·é”Ÿæˆªé�©æ‹·é”Ÿæ–¤æ‹·æ�¯
//				TranObject<User> register2TranObject = new TranObject<User>(
//						TranObjectType.REGISTER);
//				User register2user = new User();
////				register2user.setId(registerResult);
//				register2TranObject.setObject(register2user);
//				out.setMessage(register2TranObject);
//				break;
//			case LOGIN:
//				System.out.println("Am I here??");
//				System.out.println("server loginUser:");
//				User loginUser = (User) read_tranObject.getObject();
//				System.out.println("server loginUser:"+loginUser);
//				
//				if(loginUser.getName() == "Zaid") {
//					TranObject<User> o = new TranObject<User>(TranObjectType.LOGINSUCCESS);
//					User u = new User();
//					
//
//					o.setObject(u);
//					out.setMessage(o);
//				}
			/*	ArrayList<User> list = dao.login(loginUser);
				TranObject<ArrayList<User>> login2Object = new TranObject<ArrayList<User>>(
						TranObjectType.LOGIN);
				if (list != null) {// é”Ÿæ–¤æ‹·é”Ÿæ–¤æ‹·å½•é”Ÿç¼´ç™¸æ‹·
					TranObject<User> onObject = new TranObject<User>(
							TranObjectType.LOGIN);
					User login2User = new User();
					login2User.setId(loginUser.getId());
					onObject.setObject(login2User);
					for (OutputThread onOut : map.getAll()) {
						onOut.setMessage(onObject);// é”Ÿå§�æ’­ä¸€é”Ÿæ–¤æ‹·é”ŸçŸ«ä¼™æ‹·é”Ÿæ–¤æ‹·é”Ÿæ–¤æ‹·
					}
					map.add(loginUser.getId(), out);// é”Ÿé¥ºå¹¿æ’­é”Ÿæ–¤æ‹·é”ŸåŠ«æŠŠè®¹æ‹·åº”é”ŸçŸ«ä¼™æ‹·idé”Ÿæ–¤æ‹·å†™é”Ÿç«­ç¨‹è¾¾æ‹·é”Ÿæ–¤æ‹·mapé”Ÿå�«ï½�æ‹·é”Ÿçš†æ†‹æ‹·è½¬é”Ÿæ–¤æ‹·é”Ÿæ–¤æ‹·æ�¯æ—¶é”Ÿæ–¤æ‹·é”Ÿæ–¤æ‹·
					login2Object.setObject(list);// é”Ÿçª–çŒ´æ‹·é”Ÿæ–¤æ‹·é”Ÿå�«æ†‹æ‹·é”Ÿæ–¤æ‹·é”Ÿæˆªé�©æ‹·é”Ÿä¾¥è®¹æ‹·é”Ÿæ–¤æ‹·é”Ÿæ–¤æ‹·
				} else {
					login2Object.setObject(null);
				}
				out.setMessage(login2Object); */

//				System.out.println(MyDate.getDateCN() + "user"
//						+ loginUser.getId() + " is online");
//				break;
//			case LOGOUT:// é”Ÿæ–¤æ‹·é”Ÿæ–¤æ‹·é”Ÿæ–¤æ‹·é¡ºé”Ÿæ–¤æ‹·é”Ÿæ–¤æ‹·é”Ÿæ–¤æ‹·é”Ÿæ–¤æ‹·é”Ÿæ�·åŒ¡æ‹·é”Ÿæ–¤æ‹·é”Ÿæ–¤æ‹·çŠ¶æ€�é”Ÿæ–¤æ‹·å�Œæ—¶ç¾¤é”Ÿæ–¤æ‹·é”Ÿæ–¤æ‹·é”Ÿæ–¤æ‹·é”Ÿæ–¤æ‹·é”Ÿæ–¤æ‹·é”Ÿæ–¤æ‹·é”Ÿæ–¤æ‹·é”ŸçŸ«ä¼™æ‹·
//				User logoutUser = (User) read_tranObject.getObject();
//				int offId = logoutUser.getId();
//				System.out
//						.println(MyDate.getDateCN() + " é”ŸçŸ«ä¼™æ‹·é”Ÿæ–¤æ‹·" + offId + " é”Ÿæ–¤æ‹·é”Ÿæ–¤æ‹·é”Ÿæ–¤æ‹·");
//				dao.logout(offId);
//				isStart = false;// é”Ÿæ–¤æ‹·é”Ÿæ–¤æ‹·é”Ÿçš†ç¡·æ‹·é”Ÿä¾¥è®¹æ‹·å¾ªé”Ÿæ–¤æ‹·
//				map.remove(offId);// é”ŸæŽ¥ä¼™æ‹·é”Ÿæ–¤æ‹·é”Ÿæ–¤æ‹·å�±é”Ÿæ–¤æ‹·é”Ÿæ–¤æ‹·çž¥é”Ÿï¿½
//				out.setMessage(null);// é”Ÿæ–¤æ‹·è¦�é”Ÿæ–¤æ‹·é”Ÿæ–¤æ‹·ä¸€é”Ÿæ–¤æ‹·é”Ÿæ–¤æ‹·é”Ÿæ–¤æ‹·æ�¯åŽ»é”Ÿæ–¤æ‹·é”Ÿæ–¤æ‹·å†™é”Ÿç«­ç­¹æ‹·
//				out.setStart(false);// é”ŸåŠ«æ–¤æ‹·é”Ÿæ–¤æ‹·å†™é”Ÿç«­ç­¹æ‹·å¾ªé”Ÿæ–¤æ‹·
//
//				TranObject<User> offObject = new TranObject<User>(
//						TranObjectType.LOGOUT);
//				User logout2User = new User();
//				logout2User.setId(logoutUser.getId());
//				offObject.setObject(logout2User);
//				for (OutputThread offOut : map.getAll()) {// é”Ÿå§�æ’­é”ŸçŸ«ä¼™æ‹·é”Ÿæ–¤æ‹·é”Ÿæ–¤æ‹·é”Ÿæ–¤æ‹·æ�¯
//					offOut.setMessage(offObject);
//				}
//				break;
			
//			case REFRESH:
//				List<User> refreshList = dao.refresh(read_tranObject
//						.getFromUser());
//				TranObject<List<User>> refreshO = new TranObject<List<User>>(
//						TranObjectType.REFRESH);
//				refreshO.setObject(refreshList);
//				out.setMessage(refreshO);
//				break;
//			case CHOICEA:
//				if(Constants.switch1==1){
//				User studenta = (User) read_tranObject.getObject();
//				dao.setanswer(Constants.questionnum,studenta.getId());
//				}
//				break;
//			case CHOICEB:
//				if(Constants.switch1==1){
//				User studentb = (User) read_tranObject.getObject();
//				dao.setanswer(Constants.questionnum,studentb.getId());
//				}
//				break;
//			case CHOICEC:
//				if(Constants.switch1==1){
//				User studentc = (User) read_tranObject.getObject();
//				dao.setanswer(Constants.questionnum,studentc.getId());
//				}
//				break;
//			case CHOICED:
//				if(Constants.switch1==1){
//				User studentd = (User) read_tranObject.getObject();
//				dao.setanswer(Constants.questionnum,studentd.getId());
//				}
//				break;
//			case CHOICEE:
//				if(Constants.switch1==1){
//				User studente = (User) read_tranObject.getObject();
//				dao.setanswer(Constants.questionnum,studente.getId());
//				}
//				break;
			case NEXTQ:
				User student = (User) read_tranObject.getObject();
				Constants.questionnum+=1;
				Constants.switch1=1;
				 TranObject<User> onObject = new TranObject<User>(  
                         TranObjectType.NEXTQ); 
				out.setMessage(onObject);
				break;
			case CLOSEQ:
				Constants.switch1=0;
				 TranObject<User> on2Object = new TranObject<User>(  
                         TranObjectType.CLOSEQ);
				 out.setMessage(on2Object);
				break;
			case ENDGAME:
				returnObject=new TranObject<User>(TranObjectType.ENDGAME);
				requestObject = (User) read_tranObject.getObject();
				ArrayList<Player> temp = serverManager.getGameState().getListOfPlayers();
				serverManager.resetForNewGame();
				
				returnObject.setObject(requestObject);
				for(Player p: temp) {
					OutputThread onOut = map.getById(p.getID());
					onOut.setMessage(returnObject);
				}
//				out.setMessage(returnObject);
				//serverManager.getGameManager().end();
				break;
			default:
				break;
			}
		}
		//System.out.println("Did not enter IF -- exiting method");
	}
}
