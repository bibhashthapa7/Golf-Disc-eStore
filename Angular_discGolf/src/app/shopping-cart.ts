/**
 * @Purpose This is used to create a shopping cart object in typescript
 * @author Coolname
 */

export interface ShoppingCart {
    id: number
    username: string,
    contents: Map<string, Number>
}