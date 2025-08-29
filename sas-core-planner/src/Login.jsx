import React from 'react';
import { createRoot } from "react-dom/client";
import { apiUrl } from './config.js';

class LoginPage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            username: '',
            password: '',
            error: ''
        };
    }

    updateUsername = (event) => {
        this.setState({ 
            username: event.target.value,
            error: ''
        });
    }

    updatePassword = (event) => {
        this.setState({ 
            password: event.target.value,
            error: ''
        });
    }

    login = () => {
        const { username, password } = this.state;
        if (username && password) {
            fetch(`${apiUrl}/auth/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ username, password })
            })
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    this.setState({
                        username: '',
                        password: '',
                        error: 'Incorrect username or password.'
                    });
                }
            }).then(data => {
                if (data) {
                    localStorage.setItem('token', data.token);
                    localStorage.setItem('username', username);
                    window.location.href = 'courses.html';
                }
            }).catch(error => {
                this.setState({
                    username: '',
                    password: '',
                    error: 'An error occurred while trying to log in.'
                });
            });
        } else {
            this.setState({
                username: '',
                password: '',
                error: 'Please enter your username and password.'
            });
        }
    }
    render() {
        return (
            <div>
                <h1>Login</h1>
                <form>
                    <label htmlFor="username">Username: </label>
                    <input type="text" id="username" name="username" value={this.state.username} onChange={this.updateUsername} required/>
                    <br />
                    <label htmlFor="password">Password: </label>
                    <input type="password" id="password" name="password" value={this.state.password} onChange={this.updatePassword} required />
                    <br />
                    <div>
                        <button id="login" type="button" onClick={this.login}>Login</button>
                        <p id="error" style={{ color: 'red' }}>{this.state.error}</p>
                    </div>
                </form>
                <p>Don't have an account? <a href="register.html">Register here</a>.</p>
            </div>
        );
    }
}

const root = createRoot(document.getElementById('root'));
root.render(<LoginPage />);