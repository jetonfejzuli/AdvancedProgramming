package Lab5.Task1;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.TreeSet;
import java.util.stream.Collectors;

class NoSuchRoomException extends Exception {
    public NoSuchRoomException(String name) {
        super(name);
    }
}

class NoSuchUserException extends Exception {
    public NoSuchUserException(String name) {
        super(name);
    }
}

class ChatRoom {
    private String name;
    private TreeSet<String> users;

    public ChatRoom(String name) {
        this.name = name;
        this.users = new TreeSet<>();
    }

    public String getName() {
        return name;
    }

    public void addUser(String username) {
        users.add(username);
    }


    public void removeUser(String username) {
        users.remove(username);
    }

    public boolean hasUser(String username) {
        return users.contains(username);
    }

    public int numUsers() {
        return users.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("\n");
        if (users.isEmpty()) {
            sb.append("EMPTY\n");
        } else {
            users.forEach(user -> sb.append(user).append("\n"));
        }
        return sb.toString();
    }

}

class ChatSystem {
    private TreeMap<String, ChatRoom> chatRooms;
    private TreeSet<String> users;

    public ChatSystem() {
        this.chatRooms = new TreeMap<>();
        this.users = new TreeSet<>();
    }

    public void addRoom(String roomName) {
        chatRooms.put(roomName, new ChatRoom(roomName));
    }

    public void removeRoom(String roomName) {
        chatRooms.remove(roomName);
    }


    public ChatRoom getRoom(String roomName) throws NoSuchRoomException {
        if (!chatRooms.containsKey(roomName)) {
            throw new NoSuchRoomException(roomName);
        }
        return chatRooms.get(roomName);
    }

    public void register(String userName) {
        users.add(userName);
        ChatRoom target = null;
        for (ChatRoom r : chatRooms.values()) {
            if (target == null ||
                    r.numUsers() < target.numUsers() ||
                    (r.numUsers() == target.numUsers() &&
                            r.getName().compareTo(target.getName()) < 0)) {
                target = r;
            }
        }

        if (target != null)
            target.addUser(userName);
    }

    public void registerAndJoin(String userName, String roomName) {
        users.add(userName);
        chatRooms.get(roomName).addUser(userName);
    }

    public void joinRoom(String userName, String roomName) throws NoSuchRoomException, NoSuchUserException {
        if (!chatRooms.containsKey(roomName)) {
            throw new NoSuchRoomException(roomName);
        }
        if (!users.contains(userName)) {
            throw new NoSuchUserException(userName);
        }
        chatRooms.get(roomName).addUser(userName);
    }

    public void leaveRoom(String userName, String roomName) throws NoSuchRoomException, NoSuchUserException {
        if (!chatRooms.containsKey(roomName)) {
            throw new NoSuchRoomException(roomName);
        }
        if (!users.contains(userName)) {
            throw new NoSuchUserException(userName);
        }
        chatRooms.get(roomName).removeUser(userName);
    }

    public void followFriend(String username, String friend_username) throws NoSuchUserException {
        if (!users.contains(username)) {
            throw new NoSuchUserException(friend_username);
        }

        if (!users.contains(friend_username)) {
            throw new NoSuchUserException(friend_username);
        }

        for (ChatRoom room : chatRooms.values()) {
            if (room.hasUser(friend_username)) {
                room.addUser(username);
            }
        }
    }

}

public class ChatSystemTest {

    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchRoomException {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) {
            ChatRoom cr = new ChatRoom(jin.next());
            int n = jin.nextInt();
            for (int i = 0; i < n; ++i) {
                k = jin.nextInt();
                if (k == 0) cr.addUser(jin.next());
                if (k == 1) cr.removeUser(jin.next());
                if (k == 2) System.out.println(cr.hasUser(jin.next()));
            }
//            System.out.println("");
            System.out.println(cr.toString());
            n = jin.nextInt();
            if (n == 0) return;
            ChatRoom cr2 = new ChatRoom(jin.next());
            for (int i = 0; i < n; ++i) {
                k = jin.nextInt();
                if (k == 0) cr2.addUser(jin.next());
                if (k == 1) cr2.removeUser(jin.next());
                if (k == 2) cr2.hasUser(jin.next());
            }
            System.out.println(cr2.toString());
        }
        if (k == 1) {
            ChatSystem cs = new ChatSystem();
            Method mts[] = cs.getClass().getMethods();
            while (true) {
                String cmd = jin.next();
                if (cmd.equals("stop")) break;
                if (cmd.equals("print")) {
                    System.out.println(cs.getRoom(jin.next()) + "\n");
                    continue;
                }
                for (Method m : mts) {
                    if (m.getName().equals(cmd)) {
                        String params[] = new String[m.getParameterTypes().length];
                        for (int i = 0; i < params.length; ++i) params[i] = jin.next();
                        m.invoke(cs, (Object[]) params);
                    }
                }
            }
        }
    }

}

