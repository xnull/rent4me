"use strict";

class Chats extends React.Component {
    constructor(props) {
        super(props);
        this.state = {messages: []};
    }

    componentDidMount() {
        setInterval(this.tick.bind(this), 3000); // Call a method on the mixin
    }

    tick() {
        this.newMessage();
    }

    newMessage() {
        $.ajax("http://api.icndb.com/jokes/random?firstName=John&amp;lastName=Doe").done(function (data) {
            let messages = this.state.messages;
            messages.push(data.value.joke);
            this.setState({messages: messages});
        }.bind(this));
    }

    render() {
        let chatsStyle = {
            minHeight: '90%', heightAuto: '!important', height: '90%', border: '1px solid #d5dde9'
        };
        if ($.isEmptyObject(this.state.messages)) {
            return null;
        }

        return (
            <div>
                <div className="row" style={{marginBottom:'40px'}}>
                </div>
                <div className="container" style={chatsStyle}>
                    <div className="row">
                        <div className="col-md-4" style={{height:'100%',borderRight:'1px solid #d5dde9', padding: 0}}>
                            <ChatItem active={true}/>
                            <ChatItem />
                            <ChatItem />
                            <ChatItem />
                            <ChatItem />
                        </div>
                        <div className="col-md-8">
                            {this.state.messages.map(function (message, index) {
                                return <Message message={message} key={index} />
                            })}
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

class Message extends React.Component {

    render() {
        return (
            <a className="chats__item" key={this.props.key}>
                <img src="https://cdn3.iconfinder.com/data/icons/developerkit/png/Chat%20Bubble%20Alt.png"
                     className="chats__ava" alt="chat-image"/>
                <p className="chats__name">Hey hey</p>
                <span className="chats__text">{this.props.message}</span>
            </a>
        )
    }
}

class ChatItem extends React.Component {
    render() {
        let chatsClass = this.props.active ? "chats__item chats__item_active" : "chats__item";
        return (
            <a className={chatsClass}>
                <img src="https://cdn3.iconfinder.com/data/icons/developerkit/png/Chat%20Bubble%20Alt.png"
                     className="chats__ava" alt="chat-image"/>
                <p className="chats__name">Привет сосед</p>
                <span className="chats__text">Как дела?</span>
            </a>
        )
    }
}

React.render(<Chats/>, document.getElementById("chats"));