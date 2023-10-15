/**
 * @Purpose This is used to create a user in typescript
 * @author Coolname
 */

export interface User {
    id: number | null,
    username: string | null,
    password: string | null,
    isAdmin: boolean | null,
    loggedIn: boolean | null
}