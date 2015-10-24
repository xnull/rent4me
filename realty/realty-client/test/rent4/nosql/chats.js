"use strict";

class Storage {

    constructor() {
        this.storage = [];
    }

    put(element) {
        this.storage.push(element);
    }

    get(fieldName, value) {
        let result = [];
        for (let i = 0; i < this.storage.length; i++) {
            if (this.storage[i][fieldName] === value) {
                result.push(this.storage[i]);
            }
        }
        return result;
    }
}

class Chats extends React.Component {
    constructor(props){
        super(props);
        this.storage = new Storage();
        this.storage.put(new Man(1));
    }

    addIdentIntoMan(){
        let man = this.storage.get('id', 1);
        man.idents.push(123);
    }

    render() {
        return (
            <div>
                <a className="btn">find</a>
            </div>
        )
    }
}

class Man {
    constructor(id){
        this.id = id;
        this.idents = [];
    }
}

React.render(<Chats/>, document.getElementById("chats"));