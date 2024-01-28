const mainMsgArea = document.getElementById('main-msg-area');
const chatType = document.getElementById('chat-type');
const connectedUserList = document.getElementById('connected-user-list')
var stompClient = null;
var username = null;
let userListConnected = [];
var nameList = [
    'Time', 'Past', 'Future', 'Dev',
    'Fly', 'Flying', 'Soar', 'Soaring', 'Power', 'Falling',
    'Fall', 'Jump', 'Cliff', 'Mountain', 'Rend', 'Red', 'Blue',
    'Green', 'Yellow', 'Gold', 'Demon', 'Demonic', 'Panda', 'Cat',
    'Kitty', 'Kitten', 'Zero', 'Memory', 'Trooper', 'XX', 'Bandit',
    'Fear', 'Light', 'Glow', 'Tread', 'Deep', 'Deeper', 'Deepest',
    'Mine', 'Your', 'Worst', 'Enemy', 'Hostile', 'Force', 'Video',
    'Game', 'Donkey', 'Mule', 'Colt', 'Cult', 'Cultist', 'Magnum',
    'Gun', 'Assault', 'Recon', 'Trap', 'Trapper', 'Redeem', 'Code',
    'Script', 'Writer', 'Near', 'Close', 'Open', 'Cube', 'Circle',
    'Geo', 'Genome', 'Germ', 'Spaz', 'Shot', 'Echo', 'Beta', 'Alpha',
    'Gamma', 'Omega', 'Seal', 'Squid', 'Money', 'Cash', 'Lord', 'King',
    'Duke', 'Rest', 'Fire', 'Flame', 'Morrow', 'Break', 'Breaker', 'Numb',
    'Ice', 'Cold', 'Rotten', 'Sick', 'Sickly', 'Janitor', 'Camel', 'Rooster',
    'Sand', 'Desert', 'Dessert', 'Hurdle', 'Racer', 'Eraser', 'Erase', 'Big',
    'Small', 'Short', 'Tall', 'Sith', 'Bounty', 'Hunter', 'Cracked', 'Broken',
    'Sad', 'Happy', 'Joy', 'Joyful', 'Crimson', 'Destiny', 'Deceit', 'Lies',
    'Lie', 'Honest', 'Destined', 'Bloxxer', 'Hawk', 'Eagle', 'Hawker', 'Walker',
    'Zombie', 'Sarge', 'Capt', 'Captain', 'Punch', 'One', 'Two', 'Uno', 'Slice',
    'Slash', 'Melt', 'Melted', 'Melting', 'Fell', 'Wolf', 'Hound',
    'Legacy', 'Sharp', 'Dead', 'Mew', 'Chuckle', 'Bubba', 'Bubble', 'Sandwich', 'Smasher', 'Extreme', 'Multi', 'Universe', 'Ultimate', 'Death', 'Ready', 'Monkey', 'Elevator', 'Wrench', 'Grease', 'Head', 'Theme', 'Grand', 'Cool', 'Kid', 'Boy', 'Girl', 'Vortex', 'Paradox'
];

function generate() {
    return nameList[Math.floor(Math.random() * nameList.length)];
};


function sendMessage(event) {
    event.preventDefault();
    let formData = new FormData(event.target);
    let messageContent = formData.get('message');

    if (chatType.getAttribute('data-chat-type') === 'GROUP') {
        let customMessage = {
            sender: username,
            content: messageContent,
            sendDate: new Date()
        }
        if (stompClient) {
            stompClient.send("/app/public", {}, JSON.stringify(customMessage));
        }
        event.target.querySelector('input').value = '';
    } else {
        let customMessage = {
            sender: username,
            reciever: chatType.getAttribute('data-chat-type'),
            content: messageContent,
            sendDate: new Date()
        }
        if (stompClient) {
            stompClient.send("/app/private-message", {}, JSON.stringify(customMessage));
            let mainDiv = document.createElement('div');
            mainDiv.className = 'message-and-user';
            mainDiv.innerHTML = `<div class="message-details">
                            <p id="msg-box">${customMessage.content}</p>
                            <div class="date-and-time">
                                <span id="msg-date">${customMessage.sendDate}</span>
                                <p id="msg-username">
                                    ${customMessage.sender}
                                </p>
                            </div>
                        </div>`;
            mainMsgArea.append(mainDiv);
        }
        event.target.querySelector('input').value = '';
    }

}

function connectionForm(event) {
    event.preventDefault();
    let formData = new FormData(event.target);
    for (let [key, value] of formData) {
        console.log(`${key}: ${value}`)
        username = value;
    }
    connect();

}


function connect() {
    var socket = new SockJS('/my-app');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        username = generate()
        ///////////First Path Subscribe For All User Know Which user connected/////////////////
        stompClient.subscribe('/all/connected', onConnected);
        stompClient.send("/app/connected",
            {},
            JSON.stringify({ sender: username })
        );

        ///////////Second Path Subscribe For All User Know Which user Sending message/////////////////
        stompClient.subscribe('/all/public/message', onPublicMessageReceived);


        ///////////Third Path Subscribe For All User Sending message PRIVAtelly/////////////////
        stompClient.subscribe(`/user/${username}/private`, onPrivateMessageReceived);

    });
}

function onConnected(connect) {
    console.log("TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT");
    var conMessage = JSON.parse(connect.body);
    fetch('/get-connected-user').then(json => json.json()).then(data => {
        console.log(data);
        userListConnected = data;
        connectedUserList.innerHTML = '';
        userListConnected.forEach((user) => {

            let li = document.createElement('li');
            li.innerHTML = `<a href="#">
                                <h5>${user.sender}</h5>
                            </a>`;
            connectedUserList.append(li);
        })

        const userList = document.querySelectorAll('.private-user-list li');
        userList.forEach(li => {
            li.addEventListener('click', privateUserClicked)
        })
    })

}

function onPublicMessageReceived(publicMsg) {
    var message = JSON.parse(publicMsg.body);
    console.log(message);
    let mainDiv = document.createElement('div');
    mainDiv.className = 'message-and-user';
    mainDiv.innerHTML = `<div class="message-details">
                            <p id="msg-box">${message.content}</p>
                            <div class="date-and-time">
                                <span id="msg-date">${message.sendDate}</span>
                                <p id="msg-username">
                                    ${message.sender}
                                </p>
                            </div>
                        </div>`;
    mainMsgArea.append(mainDiv);
}

function onPrivateMessageReceived(privateMsg) {
    var message = JSON.parse(privateMsg.body);
    console.log(message);
    let mainDiv = document.createElement('div');
    mainDiv.className = 'message-and-user';
    mainDiv.innerHTML = `<div class="message-details">
                            <p id="msg-box">${message.content}</p>
                            <div class="date-and-time">
                                <span id="msg-date">${message.sendDate}</span>
                                <p id="msg-username">
                                    ${message.sender}
                                </p>
                            </div>
                        </div>`;
    mainMsgArea.append(mainDiv);
}

function privateUserClicked(event) {
    chatType.setAttribute('data-chat-type', event.target.textContent.trim(''))
    chatType.innerText = event.target.textContent.trim('')
}


// setInterval(()=>connect(),10000)